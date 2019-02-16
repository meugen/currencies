package meugeninua.android.currencies.ui.fragments.currencydetails.viewmodel;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Pair;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.Injector;
import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.mappers.utils.MapperUtils;

public class CurrencyDetailsViewModel extends ViewModel implements Injector {

    private final MutableLiveData<Pair<Currency, Exchange>> exchangeData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> datesData = new MutableLiveData<>();

    private ExchangeDao exchangeDao;
    private EntityMapper<Currency> currencyMapper;
    private EntityMapper<Exchange> exchangeMapper;
    private Handler workerHandler;

    private volatile Cursor currencyDetailsCursor;
    private volatile Cursor datesCursor;

    public LiveData<Pair<Currency, Exchange>> getExchange(
            final int currencyId, final String date) {
        if (currencyDetailsCursor == null) {
            workerHandler.post(() -> loadExchange(currencyId, date));
        }
        return exchangeData;
    }

    public LiveData<List<String>> getDates(final int currencyId) {
        if (datesCursor == null) {
            workerHandler.post(() -> loadDates(currencyId));
        }
        return datesData;
    }

    public void reloadExchange(final int currencyId, final String date) {
        workerHandler.post(() -> loadExchange(currencyId, date));
    }

    @WorkerThread
    private void loadExchange(final int currencyId, final String date) {
        if (currencyDetailsCursor != null) {
            currencyDetailsCursor.close();
        }
        currencyDetailsCursor = date == null
                ? exchangeDao.getLatestExchangeCursor(currencyId)
                : exchangeDao.getExchangeByDateCursor(currencyId, date);
        currencyDetailsCursor.registerContentObserver(new CurrencyDetailsContentObserver(this, currencyId, date));
        if (!currencyDetailsCursor.moveToFirst()) {
            exchangeData.postValue(null);
            return;
        }
        Currency currency = currencyMapper.cursorToEntity(currencyDetailsCursor);
        Exchange exchange = exchangeMapper.cursorToEntity(currencyDetailsCursor);
        exchangeData.postValue(new Pair<>(currency, exchange));
    }

    @WorkerThread
    private void loadDates(final int currencyId) {
        if (datesCursor != null) {
            datesCursor.close();
        }
        datesCursor = exchangeDao.getExchangeDatesCursor(currencyId);
        datesCursor.registerContentObserver(new ExchangeDatesContentObserver(this, currencyId));
        datesData.postValue(MapperUtils.cursorToStringList(datesCursor, Constants.FLD_EXCHANGE_DATE));
    }

    @Override
    public void inject(final AppComponent appComponent) {
        exchangeDao = appComponent.provideExchangeDao();
        currencyMapper = appComponent.provideCurrencyMapper();
        exchangeMapper = appComponent.provideExchangeMapper();
        workerHandler = appComponent.provideWorkerHandler();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (datesCursor != null) {
            datesCursor.close();
            datesCursor = null;
        }
        if (currencyDetailsCursor != null) {
            currencyDetailsCursor.close();
            currencyDetailsCursor = null;
        }
    }

    private static class CurrencyDetailsContentObserver extends ContentObserver {

        private final WeakReference<CurrencyDetailsViewModel> viewModelRef;
        private final int currencyId;
        private final String date;

        CurrencyDetailsContentObserver(
                final CurrencyDetailsViewModel viewModel,
                final int currencyId, final String date) {
            super(viewModel.workerHandler);
            this.viewModelRef = new WeakReference<>(viewModel);
            this.currencyId = currencyId;
            this.date = date;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            CurrencyDetailsViewModel viewModel = viewModelRef.get();
            if (viewModel == null) {
                return;
            }
            viewModel.loadExchange(currencyId, date);
        }
    }

    private static class ExchangeDatesContentObserver extends ContentObserver {

        private final WeakReference<CurrencyDetailsViewModel> viewModelRef;
        private final int currencyId;

        ExchangeDatesContentObserver(final CurrencyDetailsViewModel viewModel, final int currencyId) {
            super(viewModel.workerHandler);
            this.viewModelRef = new WeakReference<>(viewModel);
            this.currencyId = currencyId;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            CurrencyDetailsViewModel viewModel = viewModelRef.get();
            if (viewModel == null) {
                return;
            }
            viewModel.loadDates(currencyId);
        }
    }
}

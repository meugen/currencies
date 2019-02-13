package meugeninua.android.currencies.ui.fragments.currencies.viewmodel;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.Injector;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.mappers.EntityMapper;

public class CurrenciesViewModel extends ViewModel implements Injector {

    private final MutableLiveData<List<Currency>> currenciesData = new MutableLiveData<>();

    private Handler workerHandler;
    private EntityMapper<Currency> currencyMapper;
    private CurrencyDao currencyDao;

    private volatile Cursor currenciesCursor;

    public LiveData<List<Currency>> getCurrencies() {
        if (currenciesCursor == null) {
            workerHandler.post(this::loadCurrencies);
        }
        return currenciesData;
    }

    @WorkerThread
    private void loadCurrencies() {
        if (currenciesCursor != null) {
            currenciesCursor.close();
        }
        currenciesCursor = currencyDao.getCurrenciesCursor();
        currenciesCursor.registerContentObserver(new CurrenciesContentObserver(this));
        currenciesData.postValue(currencyMapper.cursorToEntityList(currenciesCursor));
    }

    @Override
    public void inject(final AppComponent appComponent) {
        workerHandler = appComponent.provideWorkerHandler();
        currencyMapper = appComponent.provideCurrencyMapper();
        currencyDao = appComponent.provideCurrencyDao();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (currenciesCursor != null) {
            currenciesCursor.close();
            currenciesCursor = null;
        }
    }

    private static class CurrenciesContentObserver extends ContentObserver {

        private final WeakReference<CurrenciesViewModel> viewModelRef;

        CurrenciesContentObserver(final CurrenciesViewModel viewModel) {
            super(viewModel.workerHandler);
            viewModelRef = new WeakReference<>(viewModel);
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
            CurrenciesViewModel viewModel = viewModelRef.get();
            if (viewModel == null) {
                return;
            }
            viewModel.loadCurrencies();
        }
    }
}

package meugeninua.android.currencies.ui.fragments.currencydetails.viewmodel;

import android.util.Pair;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.Injector;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.data.PairLiveData;
import meugeninua.android.currencies.model.data.utils.LiveDataUtils;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;

public class CurrencyDetailsViewModel extends ViewModel implements Injector {

    private LiveData<List<String>> datesLiveData;
    private LiveData<Pair<Currency, Exchange>> exchangePairLiveData;

    private CurrencyDao currencyDao;
    private ExchangeDao exchangeDao;

    public LiveData<Pair<Currency, Exchange>> getExchange(
            final int currencyId, final String date) {
        if (exchangePairLiveData == null) {
            LiveData<Currency> currencyLiveData = currencyDao.getCurrencyById(currencyId);
            LiveData<Exchange> exchangeLiveData = date == null
                    ? exchangeDao.getLatestExchange(currencyId)
                    : exchangeDao.getExchangeByDate(currencyId, date);
            exchangePairLiveData = new PairLiveData<>(currencyLiveData, exchangeLiveData);
        }
        return exchangePairLiveData;
    }

    public void clearExchange() {
        exchangePairLiveData = null;
    }

    public LiveData<List<String>> getDates(final int currencyId) {
        if (datesLiveData == null) {
            datesLiveData = exchangeDao.getExchangeDates(currencyId);
        }
        return datesLiveData;
    }

    @Override
    public void inject(final AppComponent appComponent) {
        exchangeDao = appComponent.provideExchangeDao();
        currencyDao = appComponent.provideCurrencyDao();
    }

    @Override
    protected void onCleared() {
        LiveDataUtils.clearLiveDataIfNeeded(datesLiveData, exchangePairLiveData);
    }
}

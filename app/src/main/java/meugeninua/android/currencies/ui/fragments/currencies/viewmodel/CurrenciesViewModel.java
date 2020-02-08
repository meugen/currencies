package meugeninua.android.currencies.ui.fragments.currencies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.Injector;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.data.utils.LiveDataUtils;
import meugeninua.android.currencies.model.db.entities.Currency;

import java.util.List;

public class CurrenciesViewModel extends ViewModel implements Injector {

    private LiveData<List<Currency>> currenciesLiveData;

    private CurrencyDao currencyDao;

    public LiveData<List<Currency>> getCurrencies() {
        if (currenciesLiveData == null) {
            currenciesLiveData = currencyDao.getCurrencies();
        }
        return currenciesLiveData;
    }

    @Override
    public void inject(final AppComponent appComponent) {
        currencyDao = appComponent.currencyDao.get();
    }

    @Override
    protected void onCleared() {
        LiveDataUtils.clearLiveDataIfNeeded(currenciesLiveData);
    }
}

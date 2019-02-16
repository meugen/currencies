package meugeninua.android.currencies.ui.fragments.currencies.viewmodel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.Injector;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.db.entities.Currency;

public class CurrenciesViewModel extends ViewModel implements Injector {

    private CurrencyDao currencyDao;

    public LiveData<List<Currency>> getCurrencies() {
        return currencyDao.getCurrencies();
    }

    @Override
    public void inject(final AppComponent appComponent) {
        currencyDao = appComponent.provideCurrencyDao();
    }
}

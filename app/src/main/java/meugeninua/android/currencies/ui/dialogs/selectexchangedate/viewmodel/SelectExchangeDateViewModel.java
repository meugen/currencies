package meugeninua.android.currencies.ui.dialogs.selectexchangedate.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.Injector;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.data.utils.LiveDataUtils;

public class SelectExchangeDateViewModel extends ViewModel implements Injector {

    private LiveData<List<String>> exchangeDatesData;

    private ExchangeDao exchangeDao;

    public LiveData<List<String>> getExchangeDates(final int currencyId) {
        if (exchangeDatesData == null) {
            exchangeDatesData = exchangeDao.getExchangeDates(currencyId);
        }
        return exchangeDatesData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LiveDataUtils.clearLiveDataIfNeeded(exchangeDatesData);
    }

    @Override
    public void inject(final AppComponent appComponent) {
        this.exchangeDao = appComponent.exchangeDao.get();
    }
}

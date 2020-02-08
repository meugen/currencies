package meugeninua.android.currencies.model.dao;

import androidx.lifecycle.LiveData;
import meugeninua.android.currencies.model.db.entities.Currency;

import java.util.List;

public interface CurrencyDao {

    LiveData<List<Currency>> getCurrencies();

    LiveData<Currency> getCurrencyById(int id);

    int putCurrencies(Currency... currencies);
}

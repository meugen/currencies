package meugeninua.android.currencies.model.dao;

import androidx.lifecycle.LiveData;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Currency;

public interface CurrencyDao {

    LiveData<List<Currency>> getCurrencies();

    LiveData<Currency> getCurrencyById(int id);

    int putCurrencies(Currency... currencies);
}

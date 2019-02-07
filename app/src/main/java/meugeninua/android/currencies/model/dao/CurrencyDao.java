package meugeninua.android.currencies.model.dao;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Currency;

public interface CurrencyDao {

    List<Currency> getCurrencies();

    Currency getCurrencyById(int id);

    int putCurrencies(Currency... currencies);
}

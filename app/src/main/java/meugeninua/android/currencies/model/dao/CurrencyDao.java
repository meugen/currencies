package meugeninua.android.currencies.model.dao;

import android.database.Cursor;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Currency;

public interface CurrencyDao {

    List<Currency> getCurrenciesContent();

    Cursor getCurrenciesCursor();

    Currency getCurrencyByIdContent(int id);

    Cursor getCurrencyByIdCursor(int id);

    int putCurrencies(Currency... currencies);
}

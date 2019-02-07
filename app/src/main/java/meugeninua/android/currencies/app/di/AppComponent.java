package meugeninua.android.currencies.app.di;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.readers.EntityReader;

public interface AppComponent {

    SQLiteDatabase provideDatabase();

    EntityReader<Pair<Currency, Exchange>> provideCurrencyExchangePairReader();

    CurrencyDao provideCurrencyDao();

    ExchangeDao provideExchangeDao();
}

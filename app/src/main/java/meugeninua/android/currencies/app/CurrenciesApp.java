package meugeninua.android.currencies.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.dao.impls.CurrencyDaoImpl;
import meugeninua.android.currencies.model.dao.impls.ExchangeDaoImpl;
import meugeninua.android.currencies.model.db.CurrenciesOpenHelper;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.readers.EntityReader;
import meugeninua.android.currencies.model.readers.impls.CurrencyExchangePairReader;

public class CurrenciesApp extends Application implements AppComponent {

    public static AppComponent appComponent(final Context context) {
        return (AppComponent) context.getApplicationContext();
    }

    private SQLiteDatabase database;
    private EntityReader<Pair<Currency, Exchange>> currencyExchangePairReader;
    private CurrencyDao currencyDao;
    private ExchangeDao exchangeDao;

    @Override
    public SQLiteDatabase provideDatabase() {
        if (database == null) {
            database = new CurrenciesOpenHelper(this).getWritableDatabase();
        }
        return database;
    }

    @Override
    public EntityReader<Pair<Currency, Exchange>> provideCurrencyExchangePairReader() {
        if (currencyExchangePairReader == null) {
            currencyExchangePairReader = new CurrencyExchangePairReader();
        }
        return currencyExchangePairReader;
    }

    @Override
    public CurrencyDao provideCurrencyDao() {
        if (currencyDao == null) {
            currencyDao = new CurrencyDaoImpl(getContentResolver());
        }
        return currencyDao;
    }

    @Override
    public ExchangeDao provideExchangeDao() {
        if (exchangeDao == null) {
            exchangeDao = new ExchangeDaoImpl(getContentResolver());
        }
        return exchangeDao;
    }
}

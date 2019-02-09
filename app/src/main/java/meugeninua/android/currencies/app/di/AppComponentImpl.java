package meugeninua.android.currencies.app.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import meugeninua.android.currencies.app.conf.BuildConfigurator;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.mappers.impls.CurrencyMapperImpl;
import meugeninua.android.currencies.model.mappers.impls.ExchangeMapperImpl;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.dao.impls.CurrencyDaoImpl;
import meugeninua.android.currencies.model.dao.impls.ExchangeDaoImpl;
import meugeninua.android.currencies.model.db.CurrenciesOpenHelper;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.readers.EntityReader;
import meugeninua.android.currencies.model.readers.impls.CurrencyExchangePairReader;
import okhttp3.OkHttpClient;

public class AppComponentImpl implements AppComponent {

    private final Context context;

    private SQLiteDatabase database;
    private EntityReader<Pair<Currency, Exchange>> currencyExchangePairReader;
    private EntityMapper<Currency> currencyConverter;
    private EntityMapper<Exchange> exchangeConverter;
    private CurrencyDao currencyDao;
    private ExchangeDao exchangeDao;
    private OkHttpClient okHttpClient;

    public AppComponentImpl(final Context context) {
        this.context = context;
    }

    @Override
    public Context provideAppContext() {
        return context;
    }

    @Override
    public SQLiteDatabase provideDatabase() {
        if (database == null) {
            database = new CurrenciesOpenHelper(context).getWritableDatabase();
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
    public EntityMapper<Currency> provideCurrencyMapper() {
        if (currencyConverter == null) {
            currencyConverter = new CurrencyMapperImpl();
        }
        return currencyConverter;
    }

    @Override
    public EntityMapper<Exchange> provideExchangeMapper() {
        if (exchangeConverter == null) {
            exchangeConverter = new ExchangeMapperImpl();
        }
        return exchangeConverter;
    }

    @Override
    public CurrencyDao provideCurrencyDao() {
        if (currencyDao == null) {
            currencyDao = new CurrencyDaoImpl(
                    context.getContentResolver(),
                    provideCurrencyMapper());
        }
        return currencyDao;
    }

    @Override
    public ExchangeDao provideExchangeDao() {
        if (exchangeDao == null) {
            exchangeDao = new ExchangeDaoImpl(
                    context.getContentResolver(),
                    provideExchangeMapper());
        }
        return exchangeDao;
    }

    @Override
    public OkHttpClient provideOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = createOkHttpClient();
        }
        return okHttpClient;
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        BuildConfigurator.configureOkHttpClient(builder);
        return builder.build();
    }
}

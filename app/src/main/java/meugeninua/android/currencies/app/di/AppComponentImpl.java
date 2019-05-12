package meugeninua.android.currencies.app.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Pair;

import androidx.lifecycle.ViewModelProvider;
import meugeninua.android.currencies.app.conf.BuildConfigurator;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.dao.impls.CurrencyDaoImpl;
import meugeninua.android.currencies.model.dao.impls.ExchangeDaoImpl;
import meugeninua.android.currencies.model.db.CurrenciesOpenHelper;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.mappers.impls.CurrencyMapperImpl;
import meugeninua.android.currencies.model.mappers.impls.ExchangeMapperImpl;
import meugeninua.android.currencies.model.operations.CurrencyOperations;
import meugeninua.android.currencies.model.operations.ExchangeOperations;
import meugeninua.android.currencies.model.operations.impls.CurrencyOperationsImpl;
import meugeninua.android.currencies.model.operations.impls.ExchangeOperationsImpl;
import meugeninua.android.currencies.model.readers.EntityReader;
import meugeninua.android.currencies.model.readers.impls.CurrencyExchangePairReader;
import okhttp3.OkHttpClient;

public class AppComponentImpl implements AppComponent {

    private final Context context;

    private SQLiteDatabase database;
    private EntityReader<Pair<Currency, Exchange>> currencyExchangePairReader;
    private EntityMapper<Currency> currencyConverter;
    private EntityMapper<Exchange> exchangeConverter;
    private CurrencyOperations currencyOperations;
    private ExchangeOperations exchangeOperations;
    private CurrencyDao currencyDao;
    private ExchangeDao exchangeDao;
    private OkHttpClient okHttpClient;
    private ViewModelProvider.Factory viewModelFactory;
    private Handler workerHandler;

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
    public CurrencyOperations provideCurrencyOperations() {
        if (currencyOperations == null) {
            currencyOperations = new CurrencyOperationsImpl(provideCurrencyMapper());
        }
        return currencyOperations;
    }

    @Override
    public ExchangeOperations provideExchangeOperations() {
        if (exchangeOperations == null) {
            exchangeOperations = new ExchangeOperationsImpl(provideExchangeMapper());
        }
        return exchangeOperations;
    }

    @Override
    public CurrencyDao provideCurrencyDao() {
        if (currencyDao == null) {
            currencyDao = new CurrencyDaoImpl(
                    context.getContentResolver(),
                    provideWorkerHandler(),
                    provideCurrencyMapper(),
                    provideCurrencyOperations());
        }
        return currencyDao;
    }

    @Override
    public ExchangeDao provideExchangeDao() {
        if (exchangeDao == null) {
            exchangeDao = new ExchangeDaoImpl(
                    context.getContentResolver(),
                    provideWorkerHandler(),
                    provideExchangeMapper(),
                    provideExchangeOperations());
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

    @Override
    public ViewModelProvider.Factory provideViewModelFactory() {
        if (viewModelFactory == null) {
            viewModelFactory = new InjectViewModelFactory(this);
        }
        return viewModelFactory;
    }

    @Override
    public Handler provideWorkerHandler() {
        if (workerHandler == null) {
            HandlerThread thread = new HandlerThread("worker-thread");
            thread.start();
            Looper looper = thread.getLooper();
            workerHandler = new Handler(looper);
        }
        return workerHandler;
    }
}

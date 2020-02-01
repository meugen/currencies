package meugeninua.android.currencies.app.di;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
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
import meugeninua.android.currencies.model.mappers.impls.CurrencyMapper;
import meugeninua.android.currencies.model.mappers.impls.ExchangeMapper;
import meugeninua.android.currencies.model.operations.CurrencyOperations;
import meugeninua.android.currencies.model.operations.ExchangeOperations;
import meugeninua.android.currencies.model.operations.impls.CurrencyOperationsImpl;
import meugeninua.android.currencies.model.operations.impls.ExchangeOperationsImpl;
import meugeninua.android.currencies.model.readers.EntityReader;
import meugeninua.android.currencies.model.readers.impls.CurrencyExchangePairReader;
import okhttp3.OkHttpClient;

public class AppComponent {

    private final Context context;

    public final SingleInstance<SQLiteDatabase> database;
    public final SingleInstance<EntityReader<Pair<Currency, Exchange>>> currencyExchangePairReader;
    public final SingleInstance<EntityMapper<Currency>> currencyMapper;
    public final SingleInstance<EntityMapper<Exchange>> exchangeMapper;
    public final SingleInstance<CurrencyOperations> currencyOperations;
    public final SingleInstance<ExchangeOperations> exchangeOperations;
    public final SingleInstance<CurrencyDao> currencyDao;
    public final SingleInstance<ExchangeDao> exchangeDao;
    public final SingleInstance<OkHttpClient> okHttpClient;
    public final SingleInstance<ViewModelProvider.Factory> viewModelFactory;
    public final SingleInstance<Handler> workerHandler;

    public AppComponent(final Context context) {
        this.context = context;

        database = new LazySingleInstance<>(this::createDatabase);
        currencyExchangePairReader = new LazySingleInstance<>(CurrencyExchangePairReader::new);
        currencyMapper = new LazySingleInstance<>(CurrencyMapper::new);
        exchangeMapper = new LazySingleInstance<>(ExchangeMapper::new);
        currencyOperations = new LazySingleInstance<>(this::createCurrencyOpertations);
        exchangeOperations = new LazySingleInstance<>(this::createExchangeOperations);
        currencyDao = new LazySingleInstance<>(this::createCurrencyDao);
        exchangeDao = new LazySingleInstance<>(this::createExchangeDao);
        okHttpClient = new LazySingleInstance<>(this::createOkHttpClient);
        viewModelFactory = new LazySingleInstance<>(this::createViewModelFactory);
        workerHandler = new LazySingleInstance<>(this::createWorkerHandler);
    }

    public Context provideAppContext() {
        return context;
    }

    public <VM extends ViewModel> VM provideViewModel(
            final Fragment fragment, final Class<VM> clazz) {
        return new ViewModelProvider(fragment, viewModelFactory.get()).get(clazz);
    }

    @NonNull
    private SQLiteDatabase createDatabase() {
        return new CurrenciesOpenHelper(context).getWritableDatabase();
    }

    private CurrencyOperations createCurrencyOpertations() {
        return new CurrencyOperationsImpl(currencyMapper.get());
    }

    private ExchangeOperations createExchangeOperations() {
        return new ExchangeOperationsImpl(exchangeMapper.get());
    }

    private CurrencyDao createCurrencyDao() {
        return new CurrencyDaoImpl(
                context.getContentResolver(),
                workerHandler.get(),
                currencyMapper.get(),
                currencyOperations.get()
        );
    }

    private ExchangeDao createExchangeDao() {
        return new ExchangeDaoImpl(
                context.getContentResolver(),
                workerHandler.get(),
                exchangeMapper.get(),
                exchangeOperations.get()
        );
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        BuildConfigurator.configureOkHttpClient(builder);
        return builder.build();
    }

    private ViewModelProvider.Factory createViewModelFactory() {
        return new InjectViewModelFactory(this);
    }

    private Handler createWorkerHandler() {
        HandlerThread thread = new HandlerThread("worker-thread");
        thread.start();
        Looper looper = thread.getLooper();
        return new Handler(looper);
    }
}

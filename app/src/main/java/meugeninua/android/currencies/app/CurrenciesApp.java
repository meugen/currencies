package meugeninua.android.currencies.app;

import android.app.Application;
import android.content.Context;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.work.*;
import com.squareup.leakcanary.LeakCanary;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.ComponentInjector;
import meugeninua.android.currencies.app.di.Injector;
import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.app.workers.SyncWorker;

import java.util.concurrent.TimeUnit;

public class CurrenciesApp extends Application {

    @MainThread
    public static void inject(@NonNull ComponentInjector injector) {
        inject(injector.requireContext(), injector);
    }

    public static void inject(@NonNull Context context, @NonNull Injector injector) {
        CurrenciesApp app = (CurrenciesApp) context.getApplicationContext();
        injector.inject(app.getAppComponent());
    }

    private AppComponent appComponent;

    @MainThread
    private AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = new AppComponent(this);
        }
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupSync();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void setupSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        WorkRequest request = new PeriodicWorkRequest.Builder(SyncWorker.class, Constants.SYNC_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();
        WorkManager workManager = getAppComponent().workManager.get();
        workManager.enqueue(request);
    }
}

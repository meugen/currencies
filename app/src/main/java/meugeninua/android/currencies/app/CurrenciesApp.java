package meugeninua.android.currencies.app;

import android.app.Application;

import androidx.annotation.MainThread;

import com.squareup.leakcanary.LeakCanary;

import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.ComponentInjector;

public class CurrenciesApp extends Application {

    @MainThread
    public static void inject(final ComponentInjector injector) {
        CurrenciesApp app = (CurrenciesApp) injector.requireContext()
                .getApplicationContext();
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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}

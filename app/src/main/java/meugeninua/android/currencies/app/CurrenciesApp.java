package meugeninua.android.currencies.app;

import android.app.Application;
import android.content.Context;

import androidx.annotation.MainThread;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.AppComponentImpl;

public class CurrenciesApp extends Application {

    @MainThread
    public static AppComponent appComponent(final Context context) {
        CurrenciesApp app = (CurrenciesApp) context.getApplicationContext();
        return app.getAppComponent();
    }

    private AppComponent appComponent;

    @MainThread
    private AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = new AppComponentImpl(this);
        }
        return appComponent;
    }
}

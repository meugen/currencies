package meugeninua.android.currencies.app;

import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;

import javax.inject.Inject;

import androidx.annotation.MainThread;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasContentProviderInjector;
import meugeninua.android.currencies.app.di.DaggerAppComponent;

public class CurrenciesApp extends Application implements HasActivityInjector, HasContentProviderInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject
    DispatchingAndroidInjector<ContentProvider> contentProviderInjector;

    private boolean needToInject = true;

    @Override
    public void onCreate() {
        super.onCreate();
        injectIfNeeded();
    }

    @MainThread
    private void injectIfNeeded() {
        if (needToInject) {
            DaggerAppComponent.builder()
                    .create(this).inject(this);
            needToInject = false;
        }
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<ContentProvider> contentProviderInjector() {
        injectIfNeeded();
        return contentProviderInjector;
    }
}

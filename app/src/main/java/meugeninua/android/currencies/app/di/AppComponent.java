package meugeninua.android.currencies.app.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.modules.AppModule;
import meugeninua.android.currencies.app.di.modules.ComponentsModule;

@Component(modules = { AndroidSupportInjectionModule.class,
        ComponentsModule.class, AppModule.class })
@Singleton
public interface AppComponent extends AndroidInjector<CurrenciesApp> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<CurrenciesApp> {}
}

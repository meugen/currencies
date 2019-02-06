package meugeninua.android.currencies.app.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import meugeninua.android.currencies.app.provider.CurrenciesProvider;
import meugeninua.android.currencies.ui.activities.main.MainActivity;
import meugeninua.android.currencies.ui.activities.main.MainActivityModule;

@Module
public interface ComponentsModule {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    CurrenciesProvider contributeProvider();
}

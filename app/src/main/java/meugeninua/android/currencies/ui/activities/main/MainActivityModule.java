package meugeninua.android.currencies.ui.activities.main;

import dagger.Binds;
import dagger.Module;
import meugeninua.android.currencies.ui.activities.base.BaseActivity;
import meugeninua.android.currencies.ui.activities.base.BaseActivityModule;

@Module(includes = BaseActivityModule.class)
public interface MainActivityModule {

    @Binds
    BaseActivity bindBaseActivity(MainActivity activity);
}

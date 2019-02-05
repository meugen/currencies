package meugeninua.android.currencies.ui.activities.base;

import android.content.Context;

import dagger.Binds;
import dagger.Module;
import meugeninua.android.currencies.app.di.qualifiers.ActivityContext;

@Module
public interface BaseActivityModule {

    @Binds @ActivityContext
    Context bindActivityContext(BaseActivity activity);
}

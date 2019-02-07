package meugeninua.android.currencies.app.di.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.model.db.CurrenciesOpenHelper;
import meugeninua.android.currencies.app.di.qualifiers.AppContext;

@Module
public interface AppModule {

    @Binds @AppContext
    Context bindAppContext(CurrenciesApp app);

    @Provides @Singleton
    static SQLiteDatabase provideDatabase(@AppContext Context context) {
        return new CurrenciesOpenHelper(context).getWritableDatabase();
    }
}

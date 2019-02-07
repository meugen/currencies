package meugeninua.android.currencies.ui.activities.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        inject(CurrenciesApp.appComponent(this));
        super.onCreate(savedInstanceState);
    }

    protected void inject(final AppComponent appComponent) { }
}

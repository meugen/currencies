package meugeninua.android.currencies.ui.activities.base.fragments.base;

import android.content.Context;

import androidx.fragment.app.Fragment;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        inject(CurrenciesApp.appComponent(context));
    }

    protected void inject(final AppComponent appComponent) { }
}

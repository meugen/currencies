package meugeninua.android.currencies.ui.fragments.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;

public class BaseFragment<B extends Binding> extends Fragment {

    protected B binding;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        inject(CurrenciesApp.appComponent(context));
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.attachView(view);
    }

    @Override
    public void onDestroyView() {
        binding.detachView();
        super.onDestroyView();
    }

    protected void inject(final AppComponent appComponent) { }
}

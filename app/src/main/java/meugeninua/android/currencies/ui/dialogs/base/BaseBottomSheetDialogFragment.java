package meugeninua.android.currencies.ui.dialogs.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;

public class BaseBottomSheetDialogFragment<B extends Binding> extends BottomSheetDialogFragment {

    protected B binding;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        inject(CurrenciesApp.appComponent(context));
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.attachView(view);
    }

    @Override
    public void onDestroyView() {
        binding.detachView();
        super.onDestroyView();
    }

    protected final <VM extends ViewModel> VM getViewModel(
            final ViewModelProvider.Factory factory, final Class<VM> clazz) {
        return ViewModelProviders.of(this, factory).get(clazz);
    }

    protected void inject(final AppComponent appComponent) { }
}

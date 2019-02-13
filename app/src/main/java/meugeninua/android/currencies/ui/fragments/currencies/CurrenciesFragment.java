package meugeninua.android.currencies.ui.fragments.currencies;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.fragments.base.BaseFragment;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;
import meugeninua.android.currencies.ui.fragments.currencies.binding.CurrenciesBinding;
import meugeninua.android.currencies.ui.fragments.currencies.binding.CurrenciesBindingImpl;
import meugeninua.android.currencies.ui.fragments.currencies.viewmodel.CurrenciesViewModel;

public class CurrenciesFragment extends BaseFragment<CurrenciesBinding> {

    private CurrenciesAdapter.OnCurrencyClickListener listener;
    private CurrenciesViewModel viewModel;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        listener = (CurrenciesAdapter.OnCurrencyClickListener) context;
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currencies,
                container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getCurrencies().observe(this, this::onContentLoaded);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setupRecycler(listener);
    }

    @Override
    protected void inject(final AppComponent appComponent) {
        super.inject(appComponent);
        binding = new CurrenciesBindingImpl(getContext());
        viewModel = getViewModel(appComponent.provideViewModelFactory(), CurrenciesViewModel.class);
    }

    private void onContentLoaded(final List<Currency> currencies) {
        binding.setContent(currencies);
    }
}

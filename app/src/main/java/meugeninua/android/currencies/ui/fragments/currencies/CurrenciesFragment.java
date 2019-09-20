package meugeninua.android.currencies.ui.fragments.currencies;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.ComponentInjector;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;
import meugeninua.android.currencies.ui.fragments.currencies.binding.CurrenciesBinding;
import meugeninua.android.currencies.ui.fragments.currencies.binding.CurrenciesBindingImpl;
import meugeninua.android.currencies.ui.fragments.currencies.viewmodel.CurrenciesViewModel;

public class CurrenciesFragment extends Fragment implements ComponentInjector {

    private static final String ARG_SPAN_COUNT_RES_ID = "span_count_res_id";

    public static CurrenciesFragment build(final int spanCountResId) {
        Bundle args = new Bundle();
        args.putInt(ARG_SPAN_COUNT_RES_ID, spanCountResId);

        CurrenciesFragment fragment = new CurrenciesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CurrenciesBinding binding;
    private CurrenciesAdapter.OnCurrencyClickListener listener;
    private CurrenciesViewModel viewModel;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        listener = (CurrenciesAdapter.OnCurrencyClickListener) context;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrenciesApp.inject(this);
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
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int spanCountResId = getArguments().getInt(ARG_SPAN_COUNT_RES_ID);
        binding.setupRecycler(listener, spanCountResId);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getCurrencies().observe(this, this::onContentLoaded);
    }

    @Override
    public void inject(final AppComponent appComponent) {
        binding = new CurrenciesBindingImpl(this);
        viewModel = appComponent.provideViewModel(this,
                CurrenciesViewModel.class);
    }

    private void onContentLoaded(final List<Currency> currencies) {
        binding.setContent(currencies);
    }
}

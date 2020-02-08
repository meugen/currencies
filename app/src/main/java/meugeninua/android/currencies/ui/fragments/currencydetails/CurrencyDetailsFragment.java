package meugeninua.android.currencies.ui.fragments.currencydetails;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.ComponentInjector;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.ui.dialogs.selectexchangedate.SelectExchangeDateDialog;
import meugeninua.android.currencies.ui.fragments.currencydetails.binding.CurrencyDetailsBinding;
import meugeninua.android.currencies.ui.fragments.currencydetails.binding.CurrencyDetailsBindingImpl;
import meugeninua.android.currencies.ui.fragments.currencydetails.view.CurrencyDetailsView;
import meugeninua.android.currencies.ui.fragments.currencydetails.viewmodel.CurrencyDetailsViewModel;

public class CurrencyDetailsFragment extends Fragment
        implements CurrencyDetailsView, ComponentInjector {

    private static final String ARG_CURRENCY_ID = "currency_id";
    private static final String ARG_SELECTED_DATE = "selected_date";

    public static CurrencyDetailsFragment build(final int currencyId) {
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENCY_ID, currencyId);

        CurrencyDetailsFragment fragment = new CurrencyDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CurrencyDetailsBinding binding;
    private CurrencyDetailsViewModel viewModel;
    private LiveData<Pair<Currency, Exchange>> exchangeLiveData;

    private String selectedDate;
    private int currencyId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrenciesApp.inject(this);

        this.currencyId = getArguments().getInt(ARG_CURRENCY_ID);
        if (savedInstanceState != null) {
            selectedDate = savedInstanceState.getString(ARG_SELECTED_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency_details, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setupListeners(this);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayContent();
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_SELECTED_DATE, selectedDate);
    }

    @Override
    public void onChangeExchangeDate() {
        SelectExchangeDateDialog.build(currencyId, selectedDate)
                .show(requireFragmentManager(), "select-exchange-date");
    }

    public void onExchangeDateChanged(final String exchangeDate) {
        selectedDate = exchangeDate;
        displayContent();
    }

    @Override
    public void inject(final AppComponent appComponent) {
        binding = new CurrencyDetailsBindingImpl(this);
        viewModel = appComponent.provideViewModel(this,
                CurrencyDetailsViewModel.class);
    }

    private void onContentLoaded(final Pair<Currency, Exchange> pair) {
        if (pair == null) {
            binding.displayNoContent();
            return;
        }
        selectedDate = pair.second.exchangeDate;
        binding.displayContent(pair.first, pair.second);
    }

    private void displayContent() {
        if (exchangeLiveData != null) {
            exchangeLiveData.removeObservers(this);
            viewModel.clearExchange();
        }
        exchangeLiveData = viewModel.getExchange(currencyId, selectedDate);
        exchangeLiveData.observe(this, this::onContentLoaded);
    }
}

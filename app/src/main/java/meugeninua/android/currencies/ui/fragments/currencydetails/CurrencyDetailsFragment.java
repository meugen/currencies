package meugeninua.android.currencies.ui.fragments.currencydetails;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.ui.fragments.base.BaseFragment;
import meugeninua.android.currencies.ui.fragments.currencydetails.binding.CurrencyDetailsBinding;
import meugeninua.android.currencies.ui.fragments.currencydetails.binding.CurrencyDetailsBindingImpl;
import meugeninua.android.currencies.ui.fragments.currencydetails.viewmodel.CurrencyDetailsViewModel;

public class CurrencyDetailsFragment extends BaseFragment<CurrencyDetailsBinding>
        implements CurrencyDetailsBinding.OnDateSelectedListener {

    private static final String ARG_CURRENCY_ID = "currency_id";
    private static final String ARG_SELECTED_DATE = "selected_date";

    private static final int CONTENT_LOADER_ID = 1;
    private static final int DATES_LOADER_ID = 2;

    public static CurrencyDetailsFragment build(final int currencyId) {
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENCY_ID, currencyId);

        CurrencyDetailsFragment fragment = new CurrencyDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CurrencyDetailsViewModel viewModel;
    private LiveData<Pair<Currency, Exchange>> exchangeLiveData;

    private List<String> dates;
    private String selectedDate;
    private int currencyId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currencyId = getArguments().getInt(ARG_CURRENCY_ID);
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
        binding.displayDates(Collections.emptyList(), 0);
        binding.setupDateSelectedCallback(this);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            selectedDate = savedInstanceState.getString(ARG_SELECTED_DATE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_SELECTED_DATE, selectedDate);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getDates(currencyId).observe(this, this::onDatesLoaded);
    }

    @Override
    public void onDateSelected(final String date) {
        Log.d("CurrencyDetailsFrament", "onDateSelected(\"" + date + "\") method called");
        selectedDate = date;
        displayContent();
    }

    @Override
    protected void inject(final AppComponent appComponent) {
        super.inject(appComponent);
        binding = new CurrencyDetailsBindingImpl(getContext());
        viewModel = getViewModel(appComponent.provideViewModelFactory(), CurrencyDetailsViewModel.class);
    }

    private void onContentLoaded(final Pair<Currency, Exchange> pair) {
        if (pair == null) {
            binding.displayDates(dates, -1);
            binding.displayNoContent();
            return;
        }
        binding.displayContent(
                pair.first, pair.second);
        binding.displayDates(dates, dates.indexOf(pair.second.exchangeDate));
    }

    private void onDatesLoaded(final List<String> dates) {
        this.dates = dates;
        displayContent();
    }

    private void displayContent() {
        if (exchangeLiveData != null) {
            exchangeLiveData.removeObservers(this);
        }
        exchangeLiveData = viewModel.getExchange(currencyId, selectedDate);
        exchangeLiveData.observe(this, this::onContentLoaded);
    }
}

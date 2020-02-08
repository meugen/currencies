package meugeninua.android.currencies.ui.dialogs.selectexchangedate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.ComponentInjector;
import meugeninua.android.currencies.ui.dialogs.selectexchangedate.adapters.ExchangeDatesAdapter;
import meugeninua.android.currencies.ui.dialogs.selectexchangedate.binding.SelectExchangeDateBinding;
import meugeninua.android.currencies.ui.dialogs.selectexchangedate.binding.SelectExchangeDateBindingImpl;
import meugeninua.android.currencies.ui.dialogs.selectexchangedate.viewmodel.SelectExchangeDateViewModel;

import java.util.List;

public class SelectExchangeDateDialog extends BottomSheetDialogFragment
        implements ExchangeDatesAdapter.OnExchangeDateChangedListener, ComponentInjector {

    private static final String ARG_CURRENCY_ID = "currency_id";
    private static final String ARG_SELECTED_DATE = "selected_date";

    public static SelectExchangeDateDialog build(
            final int currencyId, final String selectedDate) {
        final Bundle args = new Bundle();
        args.putInt(ARG_CURRENCY_ID, currencyId);
        args.putString(ARG_SELECTED_DATE, selectedDate);

        SelectExchangeDateDialog dialog = new SelectExchangeDateDialog();
        dialog.setArguments(args);
        return dialog;
    }

    private ExchangeDatesAdapter.OnExchangeDateChangedListener listener;
    private SelectExchangeDateBinding binding;

    private int currencyId;
    private String selectedDate;

    private SelectExchangeDateViewModel viewModel;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        listener = (ExchangeDatesAdapter.OnExchangeDateChangedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrenciesApp.inject(this);

        Bundle args = getArguments();
        this.currencyId = args.getInt(ARG_CURRENCY_ID);
        this.selectedDate = args.getString(ARG_SELECTED_DATE);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_select_exchange_date,
                container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setupRecycler(this);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getExchangeDates(currencyId).observe(this, this::onExchangeDatesLoaded);
    }

    @Override
    public void onExchangeDateChanged(final String date) {
        listener.onExchangeDateChanged(date);
        dismiss();
    }

    private void onExchangeDatesLoaded(final List<String> exchangeDates) {
        binding.displayExchangeDates(exchangeDates, selectedDate);
    }

    @Override
    public void inject(final AppComponent appComponent) {
        this.binding = new SelectExchangeDateBindingImpl(this);
        this.viewModel = appComponent.provideViewModel(this,
                SelectExchangeDateViewModel.class);
    }
}

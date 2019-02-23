package meugeninua.android.currencies.ui.dialogs.selectexchangedate.binding;

import java.util.List;

import meugeninua.android.currencies.ui.dialogs.selectexchangedate.adapters.ExchangeDatesAdapter;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;

public interface SelectExchangeDateBinding extends Binding {

    void setupRecycler(ExchangeDatesAdapter.OnExchangeDateChangedListener listener);

    void displayExchangeDates(List<String> dates, String selectedDate);
}

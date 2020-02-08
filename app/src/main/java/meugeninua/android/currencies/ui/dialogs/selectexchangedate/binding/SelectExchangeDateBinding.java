package meugeninua.android.currencies.ui.dialogs.selectexchangedate.binding;

import meugeninua.android.currencies.ui.dialogs.selectexchangedate.adapters.ExchangeDatesAdapter;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;

import java.util.List;

public interface SelectExchangeDateBinding extends Binding {

    void setupRecycler(ExchangeDatesAdapter.OnExchangeDateChangedListener listener);

    void displayExchangeDates(List<String> dates, String selectedDate);
}

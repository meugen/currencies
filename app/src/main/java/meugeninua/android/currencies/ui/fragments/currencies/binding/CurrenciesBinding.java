package meugeninua.android.currencies.ui.fragments.currencies.binding;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;

public interface CurrenciesBinding extends Binding {

    void setupRecycler(
            CurrenciesAdapter.OnCurrencyClickListener listener,
            int spanCountResId);

    void setContent(List<Currency> currencies);
}

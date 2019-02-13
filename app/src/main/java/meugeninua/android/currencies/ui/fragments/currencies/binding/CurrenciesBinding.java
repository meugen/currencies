package meugeninua.android.currencies.ui.fragments.currencies.binding;

import android.database.Cursor;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;

public interface CurrenciesBinding extends Binding {

    void setupRecycler(
            EntityMapper<Currency> currenciesMapper,
            CurrenciesAdapter.OnCurrencyClickListener listener);

    void setContent(List<Currency> currencies);
}

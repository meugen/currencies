package meugeninua.android.currencies.ui.fragments.currencies.binding;

import android.database.Cursor;

import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;

public interface CurrenciesBinding extends Binding {

    void setupRecycler(EntityMapper<Currency> currenciesMapper);

    void setContent(Cursor cursor);
}

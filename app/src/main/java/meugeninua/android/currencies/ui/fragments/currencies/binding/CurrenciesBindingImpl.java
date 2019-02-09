package meugeninua.android.currencies.ui.fragments.currencies.binding;

import android.content.Context;
import android.database.Cursor;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.ui.fragments.base.binding.BaseBinding;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;

public class CurrenciesBindingImpl extends BaseBinding implements CurrenciesBinding {

    private final Context context;

    private CurrenciesAdapter adapter;

    public CurrenciesBindingImpl(final Context context) {
        this.context = context;
    }

    @Override
    public void setupRecycler(
            final EntityMapper<Currency> currencyMapper,
            final CurrenciesAdapter.OnCurrencyClickListener listener) {
        RecyclerView recycler = get(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        adapter = new CurrenciesAdapter(context, currencyMapper, listener);
        recycler.setAdapter(adapter);
    }

    @Override
    public void setContent(final Cursor cursor) {
        adapter.swapCursor(cursor);
    }
}

package meugeninua.android.currencies.ui.fragments.currencies.binding;

import android.content.Context;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.model.db.entities.Currency;
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
            final CurrenciesAdapter.OnCurrencyClickListener listener,
            final int spanCountResId) {
        RecyclerView recycler = get(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(context,
                context.getResources().getInteger(spanCountResId)));
        recycler.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        adapter = new CurrenciesAdapter(context, listener);
        recycler.setAdapter(adapter);
    }

    @Override
    public void setContent(final List<Currency> currencies) {
        adapter.swapContent(currencies);
    }

    @Override
    public void detachView() {
        super.detachView();
        adapter = null;
    }
}

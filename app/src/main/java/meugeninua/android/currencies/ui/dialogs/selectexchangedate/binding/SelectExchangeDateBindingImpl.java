package meugeninua.android.currencies.ui.dialogs.selectexchangedate.binding;

import android.content.Context;

import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.ui.dialogs.selectexchangedate.adapters.ExchangeDatesAdapter;
import meugeninua.android.currencies.ui.fragments.base.binding.BaseBinding;

public class SelectExchangeDateBindingImpl extends BaseBinding implements SelectExchangeDateBinding {

    private final Context context;
    private ExchangeDatesAdapter adapter;

    public SelectExchangeDateBindingImpl(final Context context) {
        this.context = context;
    }

    @Override
    public void setupRecycler(final ExchangeDatesAdapter.OnExchangeDateChangedListener listener) {
        RecyclerView recycler = get(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(context,
                context.getResources().getInteger(R.integer.dialog_exchange_dates_span_count)));
        recycler.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
        adapter = new ExchangeDatesAdapter(context, listener);
        recycler.setAdapter(adapter);
    }

    @Override
    public void displayExchangeDates(final List<String> dates, final String selectedDate) {
        adapter.swapContent(dates, selectedDate);
    }
}

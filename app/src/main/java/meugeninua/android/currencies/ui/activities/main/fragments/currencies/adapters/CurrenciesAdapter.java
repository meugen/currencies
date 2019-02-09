package meugeninua.android.currencies.ui.activities.main.fragments.currencies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.activities.base.fragments.base.adapters.CursorAdapter;

public class CurrenciesAdapter extends CursorAdapter<Currency, CurrenciesAdapter.CurrencyHolder> {

    public CurrenciesAdapter(final Context context, final EntityMapper<Currency> converter) {
        super(context, converter, new ItemCallbackImpl());
    }

    @Override
    protected CurrencyHolder createViewHolder(final LayoutInflater inflater, final ViewGroup parent) {
        return new CurrencyHolder(inflater.inflate(R.layout.item_currency, parent, false));
    }

    @Override
    protected void bindViewHolder(final CurrencyHolder holder, final Currency entity) {
        holder.bind(entity);
    }

    static class CurrencyHolder extends RecyclerView.ViewHolder {

        private final TextView codeView;
        private final TextView nameView;

        CurrencyHolder(@NonNull final View itemView) {
            super(itemView);
            this.codeView = itemView.findViewById(R.id.currency_code);
            this.nameView = itemView.findViewById(R.id.currency_name);
        }

        void bind(final Currency currency) {
            codeView.setText(currency.code);
            nameView.setText(currency.name);
        }
    }

    static class ItemCallbackImpl extends DiffUtil.ItemCallback<Currency> {

        @Override
        public boolean areItemsTheSame(
                @NonNull final Currency oldItem,
                @NonNull final Currency newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(
                @NonNull final Currency oldItem,
                @NonNull final Currency newItem) {
            return ObjectsCompat.equals(oldItem, newItem);
        }
    }
}

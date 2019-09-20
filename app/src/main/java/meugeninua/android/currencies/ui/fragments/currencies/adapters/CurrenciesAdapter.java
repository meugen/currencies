package meugeninua.android.currencies.ui.fragments.currencies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

import meugeninua.android.currencies.R;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.fragments.base.adapters.CursorAdapter;

public class CurrenciesAdapter extends CursorAdapter<Currency, CurrenciesAdapter.CurrencyHolder> {

    private final OnCurrencyClickListener listener;

    public CurrenciesAdapter(
            final Context context,
            final OnCurrencyClickListener listener) {
        super(context, new ItemCallbackImpl());
        this.listener = listener;
    }

    @Override
    protected CurrencyHolder createViewHolder(
            final LayoutInflater inflater,
            final ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_currency, parent, false);
        return new CurrencyHolder(view, listener);
    }

    @Override
    protected void bindViewHolder(
            final CurrencyHolder holder,
            final Currency entity) {
        holder.bind(entity);
    }

    static class CurrencyHolder extends RecyclerView.ViewHolder {

        private final TextView codeView;
        private final TextView nameView;

        CurrencyHolder(@NonNull final View itemView, final OnCurrencyClickListener listener) {
            super(itemView);
            this.codeView = itemView.findViewById(R.id.currency_code);
            this.nameView = itemView.findViewById(R.id.currency_name);

            itemView.setOnClickListener(new CurrencyClickListener(listener));
        }

        void bind(final Currency currency) {
            itemView.setTag(currency);
            codeView.setText(currency.code);
            nameView.setText(currency.name);
        }
    }

    static class CurrencyClickListener implements View.OnClickListener {

        private final WeakReference<OnCurrencyClickListener> listenerRef;

        CurrencyClickListener(final OnCurrencyClickListener listener) {
            this.listenerRef = new WeakReference<>(listener);
        }

        @Override
        public void onClick(final View view) {
            OnCurrencyClickListener listener = listenerRef.get();
            if (listener == null) {
                return;
            }
            listener.onCurrencyClick((Currency) view.getTag());
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

    public interface OnCurrencyClickListener {

        void onCurrencyClick(Currency currency);
    }
}

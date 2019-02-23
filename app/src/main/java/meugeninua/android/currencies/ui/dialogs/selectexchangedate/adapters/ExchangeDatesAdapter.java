package meugeninua.android.currencies.ui.dialogs.selectexchangedate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.ObjectsCompat;
import androidx.recyclerview.widget.RecyclerView;
import meugeninua.android.currencies.R;

public class ExchangeDatesAdapter extends RecyclerView.Adapter<ExchangeDatesAdapter.ExchangeDateHolder> {

    private final LayoutInflater inflater;
    private final OnExchangeDateChangedListener listener;

    private List<String> exchangeDates;
    private String selectedDate;

    public ExchangeDatesAdapter(
            final Context context,
            final OnExchangeDateChangedListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.exchangeDates = Collections.emptyList();
    }

    public void swapContent(final List<String> dates, final String selectedDate) {
        this.exchangeDates = dates;
        this.selectedDate = selectedDate;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExchangeDateHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.item_exchange_date, parent, false);
        return new ExchangeDateHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ExchangeDateHolder holder, final int position) {
        holder.bindContent(exchangeDates.get(position), selectedDate);
    }

    @Override
    public int getItemCount() {
        return exchangeDates.size();
    }

    static class ExchangeDateHolder extends RecyclerView.ViewHolder {

        private final TextView exchangeDateView;
        private final ImageView selectedIconView;

        ExchangeDateHolder(final View view, final OnExchangeDateChangedListener listener) {
            super(view);
            this.exchangeDateView = view.findViewById(R.id.exchange_date);
            this.selectedIconView = view.findViewById(R.id.icon_selected);

            view.setOnClickListener(new ExchangeDateClickListener(listener));
        }

        void bindContent(final String exchangeDate, final String selectedDate) {
            itemView.setTag(exchangeDate);
            exchangeDateView.setText(exchangeDate);
            selectedIconView.setVisibility(ObjectsCompat.equals(exchangeDate, selectedDate)
                    ? View.VISIBLE : View.INVISIBLE);
        }
    }

    static class ExchangeDateClickListener implements View.OnClickListener {

        private final WeakReference<OnExchangeDateChangedListener> listenerRef;

        ExchangeDateClickListener(final OnExchangeDateChangedListener listener) {
            this.listenerRef = new WeakReference<>(listener);
        }

        @Override
        public void onClick(final View view) {
            OnExchangeDateChangedListener listener = listenerRef.get();
            if (listener == null) {
                return;
            }
            listener.onExchangeDateChanged((String) view.getTag());
        }
    }

    public interface OnExchangeDateChangedListener {

        void onExchangeDateChanged(String date);
    }
}

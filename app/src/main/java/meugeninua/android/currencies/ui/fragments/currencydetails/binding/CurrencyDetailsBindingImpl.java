package meugeninua.android.currencies.ui.fragments.currencydetails.binding;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.List;

import meugeninua.android.currencies.R;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.ui.fragments.base.binding.BaseBinding;

public class CurrencyDetailsBindingImpl extends BaseBinding implements CurrencyDetailsBinding {

    private final Context context;
    private final NumberFormat formatter;

    private ArrayAdapter<String> datesAdapter;

    public CurrencyDetailsBindingImpl(final Context context) {
        this.context = context;
        this.formatter = NumberFormat.getCurrencyInstance();
    }

    @Override
    public void setupDateSelectedCallback(final OnDateSelectedListener listener) {
        this.<Spinner>get(R.id.exchange_date_value)
                .setOnItemSelectedListener(new OnItemSelectedListenerImpl(listener));
    }

    @Override
    public void displayDates(final List<String> dates) {
        if (datesAdapter == null) {
            datesAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item,
                    android.R.id.text1);
            this.<Spinner>get(R.id.exchange_date_value).setAdapter(datesAdapter);
        }
        datesAdapter.clear();
        datesAdapter.addAll(dates);
    }

    @Override
    public void setSelectedDate(final String selectedDate) {
        int position = datesAdapter.getPosition(selectedDate);
        this.<Spinner>get(R.id.exchange_date_value).setSelection(position);
    }

    @Override
    public void displayContent(final Currency currency, final Exchange exchange) {
        this.<TextView>get(R.id.currency_code_value).setText(currency.code);
        this.<TextView>get(R.id.currency_name_value).setText(currency.name);
        formatter.setCurrency(java.util.Currency.getInstance(currency.code));
        this.<TextView>get(R.id.exchange_rate_value).setText(formatter.format(exchange.exchangeRate));
    }

    @Override
    public void displayNoContent() {
        this.<TextView>get(R.id.currency_code_value).setText(null);
        this.<TextView>get(R.id.currency_name_value).setText(null);
        this.<TextView>get(R.id.exchange_rate_value).setText(null);
    }

    private static class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {

        private final WeakReference<OnDateSelectedListener> listenerRef;

        OnItemSelectedListenerImpl(final OnDateSelectedListener listener) {
            this.listenerRef = new WeakReference<>(listener);
        }

        @Override
        public void onItemSelected(
                final AdapterView<?> adapterView,
                final View view,
                final int position,
                final long id) {
            OnDateSelectedListener listener = listenerRef.get();
            if (listener == null) {
                return;
            }
            String date = (String) adapterView.getItemAtPosition(position);
            listener.onDateSelected(date);
        }

        @Override
        public void onNothingSelected(final AdapterView<?> adapterView) {

        }
    }
}

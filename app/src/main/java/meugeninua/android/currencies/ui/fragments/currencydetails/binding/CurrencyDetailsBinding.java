package meugeninua.android.currencies.ui.fragments.currencydetails.binding;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;

public interface CurrencyDetailsBinding extends Binding {

    void setupDateSelectedCallback(OnDateSelectedListener listener);

    void displayDates(List<String> dates);

    void setSelectedDate(String selectedDate);

    void displayContent(Currency currency, Exchange exchange);

    void displayNoContent();

    interface OnDateSelectedListener {

        void onDateSelected(String date);
    }
}

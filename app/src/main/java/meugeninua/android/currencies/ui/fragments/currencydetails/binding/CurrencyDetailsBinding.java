package meugeninua.android.currencies.ui.fragments.currencydetails.binding;

import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.ui.fragments.base.binding.Binding;
import meugeninua.android.currencies.ui.fragments.currencydetails.view.CurrencyDetailsView;

public interface CurrencyDetailsBinding extends Binding {

    void setupListeners(CurrencyDetailsView view);

    void displayContent(Currency currency, Exchange exchange);

    void displayNoContent();
}

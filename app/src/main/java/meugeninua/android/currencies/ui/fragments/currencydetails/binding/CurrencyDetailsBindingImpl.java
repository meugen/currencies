package meugeninua.android.currencies.ui.fragments.currencydetails.binding;

import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.ui.fragments.base.binding.BaseBinding;
import meugeninua.android.currencies.ui.fragments.currencydetails.view.CurrencyDetailsView;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;

public class CurrencyDetailsBindingImpl extends BaseBinding implements CurrencyDetailsBinding {

    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public CurrencyDetailsBindingImpl(final Fragment fragment) {
        super(fragment);
    }

    @Override
    public void setupListeners(final CurrencyDetailsView view) {
        this.<TextView>get(R.id.exchange_date_value).setOnClickListener(new OnClickListenerImpl(view));
    }

    @Override
    public void displayContent(final Currency currency, final Exchange exchange) {
        this.<TextView>get(R.id.currency_code_value).setText(currency.code);
        this.<TextView>get(R.id.currency_name_value).setText(currency.name);
        this.<TextView>get(R.id.exchange_date_value).setText(exchange.exchangeDate);
        formatter.setCurrency(java.util.Currency.getInstance(currency.code));
        this.<TextView>get(R.id.exchange_rate_value).setText(formatter.format(exchange.exchangeRate));
    }

    @Override
    public void displayNoContent() {
        this.<TextView>get(R.id.currency_code_value).setText(null);
        this.<TextView>get(R.id.currency_name_value).setText(null);
        this.<TextView>get(R.id.exchange_date_value).setText(null);
        this.<TextView>get(R.id.exchange_rate_value).setText(null);
    }

    private static class OnClickListenerImpl implements View.OnClickListener {

        private final WeakReference<CurrencyDetailsView> currencyDetailsViewRef;

        OnClickListenerImpl(final CurrencyDetailsView view) {
            this.currencyDetailsViewRef = new WeakReference<>(view);
        }

        @Override
        public void onClick(final View view) {
            CurrencyDetailsView currencyDetailsView = currencyDetailsViewRef.get();
            if (currencyDetailsView == null) {
                return;
            }
            int viewId = view.getId();
            if (viewId == R.id.exchange_date_value) {
                currencyDetailsView.onChangeExchangeDate();
            }
        }
    }
}

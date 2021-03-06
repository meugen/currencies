package meugeninua.android.currencies.ui.activities.currencies;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.activities.currencydetails.CurrencyDetailsActivity;
import meugeninua.android.currencies.ui.fragments.currencies.CurrenciesFragment;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;

public class CurrenciesActivity extends AppCompatActivity implements
        CurrenciesAdapter.OnCurrencyClickListener {

    private static final String TAG_CURRENCIES_FRAGMENT = "currencies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies);

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(TAG_CURRENCIES_FRAGMENT) == null) {
            CurrenciesFragment fragment = CurrenciesFragment
                    .build(R.integer.activity_currencies_currencies_list_span_count);
            manager.beginTransaction()
                    .replace(R.id.currencies_content, fragment, TAG_CURRENCIES_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onCurrencyClick(Currency currency) {
        startActivity(CurrencyDetailsActivity.build(this, currency.id));
    }
}

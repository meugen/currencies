package meugeninua.android.currencies.ui.activities.currencydetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.activities.base.BaseActivity;
import meugeninua.android.currencies.ui.dialogs.selectexchangedate.adapters.ExchangeDatesAdapter;
import meugeninua.android.currencies.ui.fragments.currencies.CurrenciesFragment;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;
import meugeninua.android.currencies.ui.fragments.currencydetails.CurrencyDetailsFragment;

public class CurrencyDetailsActivity extends BaseActivity implements
        ExchangeDatesAdapter.OnExchangeDateChangedListener,
        CurrenciesAdapter.OnCurrencyClickListener {

    private static final String EXTRA_CURRENCY_ID = "currency_id";
    private static final String TAG_CURRENCIES_FRAGMENT = "currencies";
    private static final String TAG_CURRENCY_DETAILS_FRAGMENT = "currency_details";

    public static Intent build(final Context context, final int currencyId) {
        Intent intent = new Intent(context, CurrencyDetailsActivity.class);
        intent.putExtra(EXTRA_CURRENCY_ID, currencyId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_details);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (getResources().getBoolean(R.bool.display_currencies_in_details) && manager.findFragmentByTag(TAG_CURRENCIES_FRAGMENT) == null) {
            CurrenciesFragment fragment = CurrenciesFragment
                    .build(R.integer.activity_currency_details_currencies_list_span_count);
            transaction.replace(R.id.currencies_content, fragment, TAG_CURRENCIES_FRAGMENT);
        }
        if (manager.findFragmentByTag(TAG_CURRENCY_DETAILS_FRAGMENT) == null) {
            int currencyId = getIntent().getIntExtra(EXTRA_CURRENCY_ID, 0);
            transaction.replace(R.id.currency_details_content,
                    CurrencyDetailsFragment.build(currencyId), TAG_CURRENCY_DETAILS_FRAGMENT);
        }
        transaction.commit();
    }

    @Override
    public void onExchangeDateChanged(final String date) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CURRENCY_DETAILS_FRAGMENT);
        if (fragment instanceof CurrencyDetailsFragment) {
            ((CurrencyDetailsFragment) fragment).onExchangeDateChanged(date);
        }
    }

    @Override
    public void onCurrencyClick(final Currency currency) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.currency_details_content, CurrencyDetailsFragment.build(currency.id), TAG_CURRENCY_DETAILS_FRAGMENT)
                .commit();
    }
}

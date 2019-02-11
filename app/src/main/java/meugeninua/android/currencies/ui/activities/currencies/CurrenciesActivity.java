package meugeninua.android.currencies.ui.activities.currencies;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.activities.base.BaseActivity;
import meugeninua.android.currencies.ui.activities.currencydetails.CurrencyDetailsActivity;
import meugeninua.android.currencies.ui.fragments.currencies.CurrenciesFragment;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;
import meugeninua.android.currencies.ui.fragments.currencydetails.CurrencyDetailsFragment;
import meugeninua.android.currencies.ui.fragments.message.MessageFragment;

public class CurrenciesActivity extends BaseActivity implements Constants,
        CurrenciesAdapter.OnCurrencyClickListener {

    private static final String TAG_CURRENCIES_FRAGMENT = "currencies_fragment";
    private static final String ARG_CURRENT_CURRENCY_ID = "current_currency_id";

    private Integer currentCurrencyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSync();
        setContentView(R.layout.activity_currencies);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_CURRENT_CURRENCY_ID)) {
            currentCurrencyId = savedInstanceState.getInt(ARG_CURRENT_CURRENCY_ID);
        }

        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(TAG_CURRENCIES_FRAGMENT) == null) {
            manager.beginTransaction()
                    .replace(R.id.currencies_content, new CurrenciesFragment(), TAG_CURRENCIES_FRAGMENT)
                    .commit();
        }
        showCurrencyDetails(true);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentCurrencyId != null) {
            outState.putInt(ARG_CURRENT_CURRENCY_ID, currentCurrencyId);
        }
    }

    @Override
    public void onCurrencyClick(Currency currency) {
        currentCurrencyId = currency.id;
        showCurrencyDetails(false);
    }

    private void showCurrencyDetails(final boolean fragmentOnly) {
        if (getResources().getBoolean(R.bool.display_details_in_currencies)) {
            Fragment fragment = currentCurrencyId == null
                    ? MessageFragment.build(R.string.message_no_content)
                    : CurrencyDetailsFragment.build(currentCurrencyId);

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.currency_details_content, fragment)
                    .commit();
            return;
        }
        if (!fragmentOnly && currentCurrencyId != null) {
            startActivity(CurrencyDetailsActivity.build(this, currentCurrencyId));
        }
    }

    private void setupSync() {
        Account account = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        manager.addAccountExplicitly(account, null, null);

        ContentResolver.setIsSyncable(account, AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, AUTHORITY,
                Bundle.EMPTY, SYNC_INTERVAL);
    }
}

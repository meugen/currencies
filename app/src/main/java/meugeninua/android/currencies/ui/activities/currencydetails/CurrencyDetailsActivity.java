package meugeninua.android.currencies.ui.activities.currencydetails;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_details);

        this.drawerLayout = findViewById(R.id.drawer);
        if (this.drawerLayout != null) {
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                    0, 0);
            drawerLayout.addDrawerListener(drawerToggle);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (manager.findFragmentByTag(TAG_CURRENCIES_FRAGMENT) == null) {
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
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START, true);
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null) {
            drawerToggle.syncState();
        }
    }
}

package meugeninua.android.currencies.ui.activities.currencies;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.ui.activities.base.BaseActivity;
import meugeninua.android.currencies.ui.activities.currencydetails.CurrencyDetailsActivity;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;

public class CurrenciesActivity extends BaseActivity implements Constants,
        CurrenciesAdapter.OnCurrencyClickListener {

    private SQLiteDatabase database;
    private CurrencyDao currencyDao;
    private ExchangeDao exchangeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSync();
        setContentView(R.layout.activity_currencies);
    }

    @Override
    protected void inject(final AppComponent appComponent) {
        super.inject(appComponent);
        this.database = appComponent.provideDatabase();
        this.currencyDao = appComponent.provideCurrencyDao();
        this.exchangeDao = appComponent.provideExchangeDao();
    }

    @Override
    public void onCurrencyClick(final Currency currency) {
        startActivity(CurrencyDetailsActivity.build(this, currency.id));
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

package meugeninua.android.currencies.ui.activities.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import javax.inject.Inject;

import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.ui.activities.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Inject
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSync();
        setContentView(R.layout.activity_main);
    }

    private void setupSync() {
        Account account = new Account(Constants.ACCOUNT_TYPE, Constants.ACCOUNT);
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        manager.addAccountExplicitly(account, null, null);

        ContentResolver.addPeriodicSync(account, Constants.AUTHORITY,
                Bundle.EMPTY, Constants.SYNC_INTERVAL);
    }
}

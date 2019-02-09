package meugeninua.android.currencies.ui.activities.currencydetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.ui.activities.base.BaseActivity;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;
import meugeninua.android.currencies.ui.fragments.currencydetails.CurrencyDetailsFragment;

public class CurrencyDetailsActivity extends BaseActivity {

    private static final String EXTRA_CURRENCY_ID = "currency_id";

    private static final String TAG_CURRENCY_DETAILS = "currency_details";

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
        if (manager.findFragmentByTag(TAG_CURRENCY_DETAILS) == null) {
            int currencyId = getIntent().getIntExtra(EXTRA_CURRENCY_ID, 0);
            manager.beginTransaction()
                    .replace(R.id.content, CurrencyDetailsFragment.build(currencyId), TAG_CURRENCY_DETAILS)
                    .commit();
        }
    }
}

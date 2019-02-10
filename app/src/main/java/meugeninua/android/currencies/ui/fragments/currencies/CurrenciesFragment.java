package meugeninua.android.currencies.ui.fragments.currencies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.ui.activities.currencies.OnShowCurrencyDetailsListener;
import meugeninua.android.currencies.ui.fragments.base.BaseFragment;
import meugeninua.android.currencies.ui.fragments.currencies.adapters.CurrenciesAdapter;
import meugeninua.android.currencies.ui.fragments.currencies.binding.CurrenciesBinding;
import meugeninua.android.currencies.ui.fragments.currencies.binding.CurrenciesBindingImpl;

public class CurrenciesFragment extends BaseFragment<CurrenciesBinding> implements CurrenciesAdapter.OnCurrencyClickListener {

    private EntityMapper<Currency> currencyMapper;
    private OnShowCurrencyDetailsListener listener;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        listener = (OnShowCurrencyDetailsListener) context;
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currencies,
                container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        LoaderManager.getInstance(this).restartLoader(0,
                Bundle.EMPTY, new CurrenciesCallback());
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setupRecycler(currencyMapper, this);
    }

    @Override
    public void onCurrencyClick(final Currency currency) {
        listener.onShowCurrencyDetails(currency.id);
    }

    @Override
    protected void inject(final AppComponent appComponent) {
        super.inject(appComponent);
        binding = new CurrenciesBindingImpl(getContext());
        currencyMapper = appComponent.provideCurrencyMapper();
    }

    private void onContentLoaded(final Cursor cursor) {
        binding.setContent(cursor);
    }

    private class CurrenciesCallback implements LoaderManager.LoaderCallbacks<Cursor>, Constants {

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(final int id, @Nullable final Bundle args) {
            CursorLoader loader = new CursorLoader(getContext());
            loader.setUri(Uri.parse(String.format("content://%s/currencies", AUTHORITY)));
            return loader;
        }

        @Override
        public void onLoadFinished(@NonNull final Loader<Cursor> loader, final Cursor data) {
            onContentLoaded(data);
        }

        @Override
        public void onLoaderReset(@NonNull final Loader<Cursor> loader) { }
    }
}

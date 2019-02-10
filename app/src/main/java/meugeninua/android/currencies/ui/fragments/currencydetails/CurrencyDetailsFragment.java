package meugeninua.android.currencies.ui.fragments.currencydetails;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.ui.fragments.base.BaseFragment;
import meugeninua.android.currencies.ui.fragments.currencydetails.binding.CurrencyDetailsBinding;
import meugeninua.android.currencies.ui.fragments.currencydetails.binding.CurrencyDetailsBindingImpl;

public class CurrencyDetailsFragment extends BaseFragment<CurrencyDetailsBinding>
        implements CurrencyDetailsBinding.OnDateSelectedListener {

    private static final String ARG_CURRENCY_ID = "currency_id";
    private static final String ARG_SELECTED_DATE = "selected_date";

    private static final int CONTENT_LOADER_ID = 1;
    private static final int DATES_LOADER_ID = 2;

    public static CurrencyDetailsFragment build(final int currencyId) {
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENCY_ID, currencyId);

        CurrencyDetailsFragment fragment = new CurrencyDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private EntityMapper<Currency> currencyMapper;
    private EntityMapper<Exchange> exchangeMapper;

    private List<String> dates;
    private String selectedDate;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency_details, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull final View view,
            @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.displayDates(Collections.emptyList(), 0);
        binding.setupDateSelectedCallback(this);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            selectedDate = savedInstanceState.getString(ARG_SELECTED_DATE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_SELECTED_DATE, selectedDate);
    }

    @Override
    public void onStart() {
        super.onStart();
        LoaderManager.getInstance(this).restartLoader(DATES_LOADER_ID,
                getArguments(), new ExchangeCallbacksImpl());
    }

    @Override
    public void onDateSelected(final String date) {
        Log.d("CurrencyDetailsFrament", "onDateSelected(\"" + date + "\") method called");
        selectedDate = date;
        reloadContent();
    }

    @Override
    protected void inject(final AppComponent appComponent) {
        super.inject(appComponent);
        binding = new CurrencyDetailsBindingImpl(getContext());
        currencyMapper = appComponent.provideCurrencyMapper();
        exchangeMapper = appComponent.provideExchangeMapper();
    }

    private void onContentLoaded(final Cursor cursor) {
        if (!cursor.moveToFirst()) {
            binding.displayDates(dates, -1);
            binding.displayNoContent();
            return;
        }
        Exchange exchange = exchangeMapper.cursorToEntity(cursor);
        binding.displayContent(
                currencyMapper.cursorToEntity(cursor),
                exchange);
        binding.displayDates(dates, dates.indexOf(exchange.exchangeDate));
    }

    private void onDatesLoaded(final Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow(Constants.FLD_EXCHANGE_DATE);

        dates = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            dates.add(cursor.getString(index));
        }
        reloadContent();
    }

    private void reloadContent() {
        Bundle args = new Bundle(getArguments());
        args.putString(ARG_SELECTED_DATE, selectedDate);
        LoaderManager.getInstance(this).restartLoader(CONTENT_LOADER_ID,
                args, new ExchangeCallbacksImpl());
    }

    private class ExchangeCallbacksImpl implements LoaderManager.LoaderCallbacks<Cursor>, Constants {

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(final int id, @Nullable final Bundle args) {
            int currencyId = args.getInt(ARG_CURRENCY_ID);

            CursorLoader loader = new CursorLoader(getContext());
            if (id == CONTENT_LOADER_ID) {
                String date = args.getString(ARG_SELECTED_DATE);
                String pattern = date == null ? "latest" : "date/" + date;
                loader.setUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/%s",
                        AUTHORITY, currencyId, pattern)));
            } else if (id == DATES_LOADER_ID) {
                loader.setUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/dates",
                        AUTHORITY, currencyId)));
            }
            return loader;
        }

        @Override
        public void onLoadFinished(@NonNull final Loader<Cursor> loader, final Cursor data) {
            int loaderId = loader.getId();
            if (loaderId == CONTENT_LOADER_ID) {
                onContentLoaded(data);
            } else if (loaderId == DATES_LOADER_ID) {
                onDatesLoaded(data);
            }
        }

        @Override
        public void onLoaderReset(@NonNull final Loader<Cursor> loader) { }
    }
}
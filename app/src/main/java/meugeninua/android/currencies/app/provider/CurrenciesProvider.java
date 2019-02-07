package meugeninua.android.currencies.app.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;

public class CurrenciesProvider extends ContentProvider implements Constants {

    private static final int MATCH_CURRENCIES = 1;
    private static final int MATCH_CURRENCY_ID = 2;
    private static final int MATCH_EXCHANGES = 3;
    private static final int MATCH_EXCHANGE_ID = 4;
    private static final int MATCH_EXCHANGE_LATEST = 5;
    private static final int MATCH_EXCHANGE_DATE = 6;

    private SQLiteDatabase database;

    private UriMatcher matcher;

    @Override
    public void attachInfo(final Context context, final ProviderInfo info) {
        AppComponent appComponent = CurrenciesApp.appComponent(context);
        this.database = appComponent.provideDatabase();

        super.attachInfo(context, info);
    }

    @Override
    public boolean onCreate() {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "currencies", MATCH_CURRENCIES);
        matcher.addURI(AUTHORITY, "currency/#", MATCH_CURRENCY_ID);
        matcher.addURI(AUTHORITY, "currency/#/exchanges", MATCH_EXCHANGES);
        matcher.addURI(AUTHORITY, "currency/#/exchange/id/#", MATCH_EXCHANGE_ID);
        matcher.addURI(AUTHORITY, "currency/#/exchange/latest", MATCH_EXCHANGE_LATEST);
        matcher.addURI(AUTHORITY, "currency/#/exchange/date/*", MATCH_EXCHANGE_DATE);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull final Uri uri, @Nullable final String[] projection,
            @Nullable final String selection, @Nullable final String[] selectionArgs,
            @Nullable final String sortOrder) {
        final int code = matcher.match(uri);

        Cursor cursor = null;
        if (code == MATCH_CURRENCIES) {
            cursor = queryCurrencies();
        } else if (code == MATCH_CURRENCY_ID) {
            cursor = queryCurrencyId(uri);
        } else if (code == MATCH_EXCHANGES) {
            cursor = queryExchanges(uri);
        } else if (code == MATCH_EXCHANGE_ID) {
            cursor = queryExchangeById(uri);
        } else if (code == MATCH_EXCHANGE_LATEST) {
            cursor = queryLatestExchange(uri);
        } else if (code == MATCH_EXCHANGE_DATE) {
            cursor = queryExchangeByDate(uri);
        }
        return cursor;
    }

    private Cursor queryCurrencies() {
        return database.rawQuery("SELECT * FROM currencies ORDER BY name", new String[0]);
    }

    private Cursor queryCurrencyId(final Uri uri) {
        String id = uri.getLastPathSegment();
        return database.rawQuery("SELECT * FROM currencies WHERE id=? LIMIT 1", new String[] { id });
    }

    private Cursor queryExchanges(final Uri uri) {
        String id = uri.getPathSegments().get(1);
        return database.rawQuery("SELECT * FROM exchanges WHERE currency_id=? ORDER BY exchange_date DESC",
                new String[] { id });
    }

    private Cursor queryExchangeById(final Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        String currencyId = pathSegments.get(1);
        String exchangeId = pathSegments.get(4);
        return database.rawQuery("SELECT * FROM exchanges WHERE currency_id=? AND id=? LIMIT 1",
                new String[] { currencyId, exchangeId });
    }

    private Cursor queryLatestExchange(final Uri uri) {
        String id = uri.getPathSegments().get(1);
        return database.rawQuery("SELECT * FROM exchanges WHERE currency_id=? ORDER BY exchange_date DESC LIMIT 1",
                new String[] { id });
    }

    private Cursor queryExchangeByDate(final Uri uri) {
        String id = uri.getPathSegments().get(1);
        String date = uri.getPathSegments().get(4);
        return database.rawQuery("SELECT * FROM exchanges WHERE currency_id=? AND exchange_date=? LIMIT 1",
                new String[] { id, date });
    }

    @Nullable
    @Override
    public String getType(@NonNull final Uri uri) {
        String type = null;

        int code = matcher.match(uri);
        if (code == MATCH_CURRENCIES || code == MATCH_CURRENCY_ID) {
            type = "currency";
        } else if (code == MATCH_EXCHANGES || code == MATCH_EXCHANGE_DATE || code == MATCH_EXCHANGE_LATEST) {
            type = "exchange";
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull final Uri uri, @Nullable final ContentValues values) {
        Uri resultUri = null;

        int code = matcher.match(uri);
        if (code == MATCH_CURRENCIES) {
            database.insert(TBL_CURRENCIES, null, values);
            resultUri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d",
                    AUTHORITY, values.getAsInteger(FLD_ID)));
            notifyChange(resultUri, Uri.parse(String.format("content://%s/currencies", AUTHORITY)));
        } else if (code == MATCH_EXCHANGES) {
            long exchangeId = database.insert(TBL_EXCHANGES, null, values);
            resultUri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/%d",
                    AUTHORITY, values.getAsInteger(FLD_CURRENCY_ID), exchangeId));
            notifyChange(resultUri, Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchanges",
                    AUTHORITY, values.getAsInteger(FLD_CURRENCY_ID))));
        }
        return resultUri;
    }

    private void notifyChange(final Uri... uris) {
        ContentResolver resolver = getContext().getContentResolver();
        for (Uri uri : uris) {
            resolver.notifyChange(uri, null);
        }
    }

    @Override
    public int delete(@NonNull final Uri uri, @Nullable final String s, @Nullable final String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull final Uri uri, @Nullable final ContentValues contentValues, @Nullable final String s, @Nullable final String[] strings) {
        return 0;
    }
}

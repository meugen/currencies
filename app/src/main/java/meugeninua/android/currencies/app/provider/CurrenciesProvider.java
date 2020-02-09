package meugeninua.android.currencies.app.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.ComponentInjector;

import java.util.List;
import java.util.Locale;

public class CurrenciesProvider extends ContentProvider implements Constants, ComponentInjector {

    private static final int MATCH_CURRENCIES = 1;
    private static final int MATCH_CURRENCY_ID = 2;
    private static final int MATCH_EXCHANGES = 3;
    private static final int MATCH_EXCHANGE_ID = 4;
    private static final int MATCH_EXCHANGE_LATEST = 5;
    private static final int MATCH_EXCHANGE_DATE = 6;
    private static final int MATCH_EXCHANGE_DATES = 7;

    private SQLiteDatabase database;

    private UriMatcher matcher;

    @NonNull
    @Override
    public Context requireContext() {
        Context context = getContext();
        if (context == null) {
            throw new IllegalStateException("Context is not attached yet");
        }
        return context;
    }

    @Override
    public void inject(final AppComponent appComponent) {
        appComponent.initWorkManager();
        this.database = appComponent.database.get();
    }

    @Override
    public boolean onCreate() {
        CurrenciesApp.inject(this);

        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "currencies", MATCH_CURRENCIES);
        matcher.addURI(AUTHORITY, "currency/#", MATCH_CURRENCY_ID);
        matcher.addURI(AUTHORITY, "currency/#/exchanges", MATCH_EXCHANGES);
        matcher.addURI(AUTHORITY, "currency/#/exchange/id/#", MATCH_EXCHANGE_ID);
        matcher.addURI(AUTHORITY, "currency/#/exchange/latest", MATCH_EXCHANGE_LATEST);
        matcher.addURI(AUTHORITY, "currency/#/exchange/date/*", MATCH_EXCHANGE_DATE);
        matcher.addURI(AUTHORITY, "currency/#/exchange/dates", MATCH_EXCHANGE_DATES);
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
        } else if (code == MATCH_EXCHANGE_DATES) {
            cursor = queryExchangeDates(uri);
        }
        return cursor;
    }

    private void setCurrenciesNotification(final Cursor cursor) {
        cursor.setNotificationUri(getContext().getContentResolver(),
                Uri.parse(String.format("content://%s/currencies", AUTHORITY)));
    }

    private Cursor queryCurrencies() {
        Cursor cursor = database.rawQuery("SELECT * FROM currencies ORDER BY name", new String[0]);
        setCurrenciesNotification(cursor);
        return cursor;
    }

    private Cursor queryCurrencyId(final Uri uri) {
        String id = uri.getLastPathSegment();
        Cursor cursor = database.rawQuery("SELECT * FROM currencies WHERE id=? LIMIT 1", new String[] { id });
        setCurrenciesNotification(cursor);
        return cursor;
    }

    private void setExchangesNotification(final Cursor cursor, final String currencyId) {
        cursor.setNotificationUri(getContext().getContentResolver(),
                Uri.parse(String.format("content://%s/currency/%s/exchanges", AUTHORITY, currencyId)));
    }

    private Cursor queryExchanges(final Uri uri) {
        String id = uri.getPathSegments().get(1);
        Cursor cursor = database.rawQuery("SELECT currency_id, exchange_date, exchange_rate" +
                        " FROM exchanges WHERE currency_id=? ORDER BY exchange_date DESC",
                new String[] { id });
        setExchangesNotification(cursor, id);
        return cursor;
    }

    private Cursor queryExchangeById(final Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        String currencyId = pathSegments.get(1);
        String exchangeId = pathSegments.get(4);
        Cursor cursor = database.rawQuery("SELECT currency_id, exchange_date, exchange_rate" +
                        " FROM exchanges WHERE currency_id=? AND id=? LIMIT 1",
                new String[] { currencyId, exchangeId });
        setExchangesNotification(cursor, currencyId);
        return cursor;
    }

    private Cursor queryLatestExchange(final Uri uri) {
        String id = uri.getPathSegments().get(1);
        Cursor cursor = database.rawQuery("SELECT currency_id, exchange_date, exchange_rate" +
                        " FROM exchanges WHERE currency_id=? ORDER BY id DESC LIMIT 1",
                new String[] { id });
        setExchangesNotification(cursor, id);
        return cursor;
    }

    private Cursor queryExchangeByDate(final Uri uri) {
        String id = uri.getPathSegments().get(1);
        String date = uri.getPathSegments().get(4);
        Cursor cursor = database.rawQuery("SELECT currency_id, exchange_date, exchange_rate" +
                        " FROM exchanges WHERE currency_id=? AND exchange_date=? LIMIT 1",
                new String[] { id, date });
        setExchangesNotification(cursor, id);
        return cursor;
    }

    private Cursor queryExchangeDates(final Uri uri) {
        String id = uri.getPathSegments().get(1);
        Cursor cursor = database.rawQuery("SELECT DISTINCT exchange_date FROM exchanges WHERE currency_id=?",
                new String[] { id });
        setExchangesNotification(cursor, id);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull final Uri uri) {
        String type = null;

        int code = matcher.match(uri);
        if (code == MATCH_CURRENCIES || code == MATCH_CURRENCY_ID) {
            type = "currency";
        } else if (code == MATCH_EXCHANGES || code == MATCH_EXCHANGE_DATE
                || code == MATCH_EXCHANGE_LATEST || code == MATCH_EXCHANGE_ID
                || code == MATCH_EXCHANGE_DATES) {
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
            database.insertWithOnConflict(TBL_CURRENCIES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            resultUri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d",
                    AUTHORITY, values.getAsInteger(FLD_ID)));
            notifyChange(resultUri, Uri.parse(String.format("content://%s/currencies", AUTHORITY)));
        } else if (code == MATCH_EXCHANGES) {
            long exchangeId = database.insertWithOnConflict(TBL_EXCHANGES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
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

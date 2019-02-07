package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.db.entities.Exchange;

public class ExchangeDaoImpl extends AbstractDaoImpl<Exchange> implements ExchangeDao {

    public ExchangeDaoImpl(final ContentResolver resolver) {
        super(resolver);
    }

    @Override
    Exchange cursorToEntity(final Cursor cursor) {
        Exchange exchange = new Exchange();
        exchange.currencyId = cursor.getInt(cursor.getColumnIndexOrThrow(FLD_CURRENCY_ID));
        exchange.exchangeDate = cursor.getString(cursor.getColumnIndexOrThrow(FLD_EXCHANGE_DATE));
        exchange.exchangeRate = cursor.getDouble(cursor.getColumnIndexOrThrow(FLD_EXCHANGE_RATE));
        return exchange;
    }

    @Override
    ContentValues entityToValues(final Exchange entity) {
        ContentValues values = new ContentValues();
        values.put(FLD_CURRENCY_ID, entity.currencyId);
        values.put(FLD_EXCHANGE_DATE, entity.exchangeDate);
        values.put(FLD_EXCHANGE_RATE, entity.exchangeRate);
        return values;
    }

    @Override
    public List<Exchange> getExchanges(final int currencyId) {
        Uri uri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchanges",
                AUTHORITY, currencyId));
        return getList(uri);
    }

    @Override
    public Exchange getExchangeById(final int currencyId, final int exchangeId) {
        Uri uri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/id/%d",
                AUTHORITY, currencyId, exchangeId));
        return getSingle(uri);
    }

    @Override
    public Exchange getLatestExchange(final int currencyId) {
        Uri uri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/latest",
                AUTHORITY, currencyId));
        return getSingle(uri);
    }

    @Override
    public Exchange getExchangeByDate(final int currencyId, final String date) {
        Uri uri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/date/%s",
                AUTHORITY, currencyId, date));
        return getSingle(uri);
    }

    @Override
    public int putExchanges(final int currencyId, final Exchange... exchanges) {
        Uri uri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchanges",
                AUTHORITY, currencyId));
        return putEntities(uri, exchanges);
    }
}

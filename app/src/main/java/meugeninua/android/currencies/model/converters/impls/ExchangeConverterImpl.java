package meugeninua.android.currencies.model.converters.impls;

import android.content.ContentValues;
import android.database.Cursor;

import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.converters.EntityConverter;
import meugeninua.android.currencies.model.db.entities.Exchange;

public class ExchangeConverterImpl implements EntityConverter<Exchange>, Constants {

    @Override
    public Exchange cursorToEntity(final Cursor cursor) {
        Exchange exchange = new Exchange();
        exchange.currencyId = cursor.getInt(cursor.getColumnIndexOrThrow(FLD_CURRENCY_ID));
        exchange.exchangeDate = cursor.getString(cursor.getColumnIndexOrThrow(FLD_EXCHANGE_DATE));
        exchange.exchangeRate = cursor.getDouble(cursor.getColumnIndexOrThrow(FLD_EXCHANGE_RATE));
        return exchange;
    }

    @Override
    public ContentValues entityToValues(final Exchange entity) {
        ContentValues values = new ContentValues();
        values.put(FLD_CURRENCY_ID, entity.currencyId);
        values.put(FLD_EXCHANGE_DATE, entity.exchangeDate);
        values.put(FLD_EXCHANGE_RATE, entity.exchangeRate);
        return values;
    }
}

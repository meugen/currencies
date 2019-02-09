package meugeninua.android.currencies.model.converters.impls;

import android.content.ContentValues;
import android.database.Cursor;

import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.converters.EntityConverter;
import meugeninua.android.currencies.model.db.entities.Currency;

public class CurrencyConverterImpl implements EntityConverter<Currency>, Constants {

    @Override
    public Currency cursorToEntity(final Cursor cursor) {
        Currency currency = new Currency();
        currency.id = cursor.getInt(cursor.getColumnIndexOrThrow(FLD_ID));
        currency.code = cursor.getString(cursor.getColumnIndexOrThrow(FLD_NAME));
        currency.name = cursor.getString(cursor.getColumnIndexOrThrow(FLD_NAME));
        return currency;
    }

    @Override
    public ContentValues entityToValues(final Currency entity) {
        final ContentValues values = new ContentValues();
        values.put(FLD_ID, entity.id);
        values.put(FLD_CODE, entity.code);
        values.put(FLD_NAME, entity.name);
        return values;
    }
}

package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;
import java.util.Locale;

import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.db.entities.Currency;

public class CurrencyDaoImpl extends AbstractDaoImpl<Currency> implements CurrencyDao {

    public CurrencyDaoImpl(final ContentResolver resolver) {
        super(resolver);
    }

    @Override
    Currency cursorToEntity(final Cursor cursor) {
        Currency currency = new Currency();
        currency.id = cursor.getInt(cursor.getColumnIndexOrThrow(FLD_ID));
        currency.code = cursor.getString(cursor.getColumnIndexOrThrow(FLD_NAME));
        currency.name = cursor.getString(cursor.getColumnIndexOrThrow(FLD_NAME));
        return currency;
    }

    @Override
    ContentValues entityToValues(final Currency currency) {
        final ContentValues values = new ContentValues();
        values.put(FLD_ID, currency.id);
        values.put(FLD_CODE, currency.code);
        values.put(FLD_NAME, currency.name);
        return values;
    }

    @Override
    public List<Currency> getCurrencies() {
        Uri uri = Uri.parse(String.format("content://%s/currencies", AUTHORITY));
        return getList(uri);
    }

    @Override
    public Currency getCurrencyById(final int id) {
        Uri uri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d",
                AUTHORITY, id));
        return getSingle(uri);
    }

    @Override
    public int putCurrencies(final Currency... currencies) {
        Uri uri = Uri.parse(String.format("content://%s/currencies", AUTHORITY));
        return putEntities(uri, currencies);
    }
}

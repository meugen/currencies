package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;
import java.util.Locale;

import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.mappers.EntityMapper;

public class CurrencyDaoImpl extends AbstractDaoImpl<Currency> implements CurrencyDao {

    private final EntityMapper<Currency> mapper;

    public CurrencyDaoImpl(final ContentResolver resolver, final EntityMapper<Currency> mapper) {
        super(resolver);
        this.mapper = mapper;
    }

    @Override
    public List<Currency> getCurrenciesContent() {
        try (Cursor cursor = getCurrenciesCursor()) {
            return mapper.cursorToEntityList(cursor);
        }
    }

    @Override
    public Cursor getCurrenciesCursor() {
        return queryUri(Uri.parse(String.format("content://%s/currencies", AUTHORITY)));
    }

    @Override
    public Currency getCurrencyByIdContent(final int id) {
        try (Cursor cursor = getCurrencyByIdCursor(id)) {
            return mapper.cursorToSingleEntity(cursor);
        }
    }

    @Override
    public Cursor getCurrencyByIdCursor(int id) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d",
                AUTHORITY, id)));
    }

    @Override
    public int putCurrencies(final Currency... currencies) {
        Uri uri = Uri.parse(String.format("content://%s/currencies", AUTHORITY));
        return putEntities(uri, currencies, mapper);
    }
}

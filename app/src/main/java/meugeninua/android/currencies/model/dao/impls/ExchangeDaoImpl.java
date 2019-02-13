package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;
import java.util.Locale;

import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.mappers.EntityMapper;

public class ExchangeDaoImpl extends AbstractDaoImpl<Exchange> implements ExchangeDao {

    private final EntityMapper<Exchange> mapper;

    public ExchangeDaoImpl(final ContentResolver resolver, final EntityMapper<Exchange> mapper) {
        super(resolver);
        this.mapper = mapper;
    }

    @Override
    public List<Exchange> getExchangesContent(final int currencyId) {
        try (Cursor cursor = getExchangesCursor(currencyId)) {
            return mapper.cursorToEntityList(cursor);
        }
    }

    @Override
    public Cursor getExchangesCursor(int currencyId) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchanges",
                AUTHORITY, currencyId)));
    }

    @Override
    public Exchange getExchangeByIdContent(final int currencyId, final int exchangeId) {
        try (Cursor cursor = getExchangeByIdCursor(currencyId, exchangeId)) {
            return mapper.cursorToSingleEntity(cursor);
        }
    }

    @Override
    public Cursor getExchangeByIdCursor(int currencyId, int exchangeId) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/id/%d",
                AUTHORITY, currencyId, exchangeId)));
    }

    @Override
    public Exchange getLatestExchangeContent(final int currencyId) {
        try (Cursor cursor = getLatestExchangeCursor(currencyId)) {
            return mapper.cursorToSingleEntity(cursor);
        }
    }

    @Override
    public Cursor getLatestExchangeCursor(int currencyId) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/latest",
                AUTHORITY, currencyId)));
    }

    @Override
    public Exchange getExchangeByDateContent(final int currencyId, final String date) {
        try (Cursor cursor = getExchangeByDateCursor(currencyId, date)) {
            return mapper.cursorToSingleEntity(cursor);
        }
    }

    @Override
    public Cursor getExchangeByDateCursor(int currencyId, String date) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/date/%s",
                AUTHORITY, currencyId, date)));
    }

    @Override
    public int putExchanges(final int currencyId, final Exchange... exchanges) {
        Uri uri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchanges",
                AUTHORITY, currencyId));
        return putEntities(uri, exchanges, mapper);
    }
}

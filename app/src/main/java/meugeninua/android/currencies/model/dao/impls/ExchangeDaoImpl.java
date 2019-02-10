package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.net.Uri;

import java.util.List;
import java.util.Locale;

import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.mappers.EntityMapper;

public class ExchangeDaoImpl extends AbstractDaoImpl<Exchange> implements ExchangeDao {

    public ExchangeDaoImpl(final ContentResolver resolver, final EntityMapper<Exchange> converter) {
        super(resolver, converter);
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

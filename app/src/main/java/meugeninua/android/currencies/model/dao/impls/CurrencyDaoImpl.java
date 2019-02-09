package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.net.Uri;

import java.util.List;
import java.util.Locale;

import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.db.entities.Currency;

public class CurrencyDaoImpl extends AbstractDaoImpl<Currency> implements CurrencyDao {

    public CurrencyDaoImpl(final ContentResolver resolver, final EntityMapper<Currency> converter) {
        super(resolver, converter);
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

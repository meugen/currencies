package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import androidx.lifecycle.LiveData;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.operations.CurrencyOperations;

import java.util.List;
import java.util.Locale;

public class CurrencyDaoImpl extends AbstractDaoImpl implements CurrencyDao {

    private final EntityMapper<Currency> mapper;
    private final CurrencyOperations currencyOperations;

    public CurrencyDaoImpl(
            final ContentResolver resolver,
            final Handler workerHandler,
            final EntityMapper<Currency> mapper,
            final CurrencyOperations currencyOperations) {
        super(resolver, workerHandler);
        this.mapper = mapper;
        this.currencyOperations = currencyOperations;
    }

    @Override
    public LiveData<List<Currency>> getCurrencies() {
        return queryUri(Uri.parse(String.format("content://%s/currencies", AUTHORITY)))
                .buildEntityListLiveData(mapper);
    }

    @Override
    public LiveData<Currency> getCurrencyById(int id) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d",
                AUTHORITY, id))).buildSingleEntityLiveData(mapper);
    }

    @Override
    public int putCurrencies(final Currency... currencies) {
        return putEntities(currencyOperations.putCurrencies(currencies));
    }
}

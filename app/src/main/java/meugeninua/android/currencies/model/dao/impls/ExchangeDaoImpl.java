package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import androidx.lifecycle.LiveData;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.operations.ExchangeOperations;

import java.util.List;
import java.util.Locale;

public class ExchangeDaoImpl extends AbstractDaoImpl implements ExchangeDao {

    private final EntityMapper<Exchange> mapper;
    private final ExchangeOperations exchangeOperations;

    public ExchangeDaoImpl(
            final ContentResolver resolver,
            final Handler workerHandler,
            final EntityMapper<Exchange> mapper,
            final ExchangeOperations exchangeOperations) {
        super(resolver, workerHandler);
        this.mapper = mapper;
        this.exchangeOperations = exchangeOperations;
    }

    @Override
    public LiveData<List<Exchange>> getExchanges(int currencyId) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchanges",
                AUTHORITY, currencyId))).buildEntityListLiveData(mapper);
    }

    @Override
    public LiveData<Exchange> getExchangeById(int currencyId, int exchangeId) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/id/%d",
                AUTHORITY, currencyId, exchangeId))).buildSingleEntityLiveData(mapper);
    }

    @Override
    public LiveData<Exchange> getLatestExchange(int currencyId) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/latest",
                AUTHORITY, currencyId))).buildSingleEntityLiveData(mapper);
    }

    @Override
    public LiveData<Exchange> getExchangeByDate(int currencyId, String date) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/date/%s",
                AUTHORITY, currencyId, date))).buildSingleEntityLiveData(mapper);
    }

    @Override
    public LiveData<List<String>> getExchangeDates(int currencyId) {
        return queryUri(Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchange/dates",
                AUTHORITY, currencyId))).buildStringListLiveData(FLD_EXCHANGE_DATE);
    }

    @Override
    public int putExchanges(final int currencyId, final Exchange... exchanges) {
        return putEntities(exchangeOperations.putExchanges(currencyId, exchanges));
    }
}

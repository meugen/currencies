package meugeninua.android.currencies.model.operations.impls;

import android.content.ContentProviderOperation;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.operations.ExchangeOperations;

public class ExchangeOperationsImpl implements ExchangeOperations, Constants {

    private final EntityMapper<Exchange> exchangeMapper;

    public ExchangeOperationsImpl(final EntityMapper<Exchange> exchangeMapper) {
        this.exchangeMapper = exchangeMapper;
    }

    @Override
    public List<ContentProviderOperation> putExchanges(final int currencyId, final Exchange... exchanges) {
        List<ContentProviderOperation> result = new ArrayList<>(exchanges.length);

        Uri uri = Uri.parse(String.format(Locale.ENGLISH, "content://%s/currency/%d/exchanges",
                AUTHORITY, currencyId));
        for (Exchange exchange : exchanges) {
            ContentProviderOperation operation = ContentProviderOperation.newInsert(uri)
                    .withValues(exchangeMapper.entityToValues(exchange))
                    .build();
            result.add(operation);
        }
        return result;
    }
}

package meugeninua.android.currencies.model.operations.impls;

import android.content.ContentProviderOperation;
import android.net.Uri;
import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.mappers.EntityMapper;
import meugeninua.android.currencies.model.operations.CurrencyOperations;

import java.util.ArrayList;
import java.util.List;

public class CurrencyOperationsImpl implements CurrencyOperations, Constants {

    private final EntityMapper<Currency> currencyMapper;

    public CurrencyOperationsImpl(final EntityMapper<Currency> currencyMapper) {
        this.currencyMapper = currencyMapper;
    }

    @Override
    public List<ContentProviderOperation> putCurrencies(final Currency... currencies) {
        List<ContentProviderOperation> result = new ArrayList<>(currencies.length);

        Uri uri = Uri.parse(String.format("content://%s/currencies", AUTHORITY));
        for (Currency currency : currencies) {
            ContentProviderOperation operation = ContentProviderOperation.newInsert(uri)
                    .withValues(currencyMapper.entityToValues(currency))
                    .build();
            result.add(operation);
        }
        return result;
    }
}

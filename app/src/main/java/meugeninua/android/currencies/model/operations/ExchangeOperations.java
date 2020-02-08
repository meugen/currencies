package meugeninua.android.currencies.model.operations;

import android.content.ContentProviderOperation;
import meugeninua.android.currencies.model.db.entities.Exchange;

import java.util.List;

public interface ExchangeOperations {

    List<ContentProviderOperation> putExchanges(int currencyId, Exchange... exchanges);
}

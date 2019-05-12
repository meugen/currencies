package meugeninua.android.currencies.model.operations;

import android.content.ContentProviderOperation;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Exchange;

public interface ExchangeOperations {

    List<ContentProviderOperation> putExchanges(int currencyId, Exchange... exchanges);
}

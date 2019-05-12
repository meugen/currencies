package meugeninua.android.currencies.model.operations;

import android.content.ContentProviderOperation;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Currency;

public interface CurrencyOperations {

    List<ContentProviderOperation> putCurrencies(Currency... currencies);
}

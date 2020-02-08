package meugeninua.android.currencies.model.operations;

import android.content.ContentProviderOperation;
import meugeninua.android.currencies.model.db.entities.Currency;

import java.util.List;

public interface CurrencyOperations {

    List<ContentProviderOperation> putCurrencies(Currency... currencies);
}

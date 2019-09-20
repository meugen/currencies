package meugeninua.android.currencies.model.dao;

import androidx.lifecycle.LiveData;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Exchange;

public interface ExchangeDao {

    LiveData<List<Exchange>> getExchanges(int currencyId);

    LiveData<Exchange> getExchangeById(int currencyId, int exchangeId);

    LiveData<Exchange> getLatestExchange(int currencyId);

    LiveData<Exchange> getExchangeByDate(int currencyId, String date);

    LiveData<List<String>> getExchangeDates(int currencyId);

    int putExchanges(int currencyId, Exchange... exchanges);
}

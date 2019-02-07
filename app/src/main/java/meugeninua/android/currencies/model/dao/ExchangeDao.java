package meugeninua.android.currencies.model.dao;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Exchange;

public interface ExchangeDao {

    List<Exchange> getExchanges(int currencyId);

    Exchange getExchangeById(int currencyId, int exchangeId);

    Exchange getLatestExchange(int currencyId);

    Exchange getExchangeByDate(int currencyId, String date);

    int putExchanges(int currencyId, Exchange... exchanges);
}

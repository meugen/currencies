package meugeninua.android.currencies.model.dao;

import android.database.Cursor;

import java.util.List;

import meugeninua.android.currencies.model.db.entities.Exchange;

public interface ExchangeDao {

    List<Exchange> getExchangesContent(int currencyId);

    Cursor getExchangesCursor(int currencyId);

    Exchange getExchangeByIdContent(int currencyId, int exchangeId);

    Cursor getExchangeByIdCursor(int currencyId, int exchangeId);

    Exchange getLatestExchangeContent(int currencyId);

    Cursor getLatestExchangeCursor(int currencyId);

    Exchange getExchangeByDateContent(int currencyId, String date);

    Cursor getExchangeByDateCursor(int currencyId, String date);

    int putExchanges(int currencyId, Exchange... exchanges);
}

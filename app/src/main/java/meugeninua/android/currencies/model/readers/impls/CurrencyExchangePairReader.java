package meugeninua.android.currencies.model.readers.impls;

import android.util.Pair;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class CurrencyExchangePairReader extends AbstractEntityReader<Pair<Currency, Exchange>> {

    private static final String CURRENCY_TAG = "currency";
    private static final String R030_TAG = "r030";
    private static final String TXT_TAG = "txt";
    private static final String RATE_TAG = "rate";
    private static final String CC_TAG = "cc";
    private static final String EXCHANGE_DATE_TAG = "exchangedate";

    public CurrencyExchangePairReader() {
        super(CURRENCY_TAG);
    }

    @Override
    protected Pair<Currency, Exchange> readOne(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, CURRENCY_TAG);

        final Currency currency = new Currency();
        final Exchange exchange = new Exchange();
        while (true) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.END_TAG && CURRENCY_TAG.equals(parser.getName())) {
                break;
            }
            if (eventType != XmlPullParser.START_TAG) {
                continue;
            }
            final String name = parser.getName();
            if (R030_TAG.equals(name)) {
                currency.id = Integer.parseInt(parser.nextText());
                exchange.currencyId = currency.id;
            } else if (TXT_TAG.equals(name)) {
                currency.name = parser.nextText();
            } else if (RATE_TAG.equals(name)) {
                exchange.exchangeRate = Double.parseDouble(parser.nextText());
            } else if (CC_TAG.equals(name)) {
                currency.code = parser.nextText();
            } else if (EXCHANGE_DATE_TAG.equals(name)) {
                exchange.exchangeDate = parser.nextText();
            }
        }
        return new Pair<>(currency, exchange);
    }
}

package meugeninua.android.currencies.app.provider;

import java.util.concurrent.TimeUnit;

public interface Constants {

    String AUTHORITY = "meugeninua.android.currencies";

    long SYNC_INTERVAL_SECONDS = TimeUnit.HOURS.toSeconds(1);

    String TBL_CURRENCIES = "currencies";
    String TBL_EXCHANGES = "exchanges";

    String FLD_ID = "id";
    String FLD_NAME = "name";
    String FLD_CODE = "code";
    String FLD_CURRENCY_ID = "currency_id";
    String FLD_EXCHANGE_DATE = "exchange_date";
    String FLD_EXCHANGE_RATE = "exchange_rate";
}

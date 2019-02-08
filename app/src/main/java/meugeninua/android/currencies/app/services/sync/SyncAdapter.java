package meugeninua.android.currencies.app.services.sync;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import androidx.core.app.NotificationCompat;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.model.dao.CurrencyDao;
import meugeninua.android.currencies.model.dao.ExchangeDao;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.readers.EntityReader;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String SYNC_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final OkHttpClient client;
    private final CurrencyDao currencyDao;
    private final ExchangeDao exchangeDao;
    private final EntityReader<Pair<Currency, Exchange>> currencyExchangePairReader;

    SyncAdapter(final AppComponent appComponent) {
        super(appComponent.provideAppContext(), true);
        this.client = appComponent.provideOkHttpClient();
        this.currencyDao = appComponent.provideCurrencyDao();
        this.exchangeDao = appComponent.provideExchangeDao();
        this.currencyExchangePairReader = appComponent.provideCurrencyExchangePairReader();
    }

    @Override
    public void onPerformSync(
            final Account account, final Bundle extras, final String authority,
            final ContentProviderClient contentProviderClient, final SyncResult syncResult) {
        try {
            Request request = new Request.Builder()
                    .get().url(SYNC_URL).build();
            processCall(client.newCall(request), syncResult);
            showNotification();
        } catch (IOException e) {
            syncResult.stats.numIoExceptions++;
            Log.e("SyncAdapter", e.getMessage(), e);
        } catch (XmlPullParserException e) {
            syncResult.stats.numParseExceptions++;
            Log.e("SyncAdapter", e.getMessage(), e);
        }
    }

    private void showNotification() {
        final Context context = getContext();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("sync",
                    "Sync channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(context, "sync")
                .setContentTitle("Sync is done")
                .setContentText(DateFormat.getDateTimeInstance().format(new Date()))
                .setSmallIcon(R.drawable.baseline_done_outline_black_18)
                .build();
        manager.notify(0, notification);
    }

    private void processCall(final Call call, final SyncResult syncResult) throws XmlPullParserException, IOException {
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                return;
            }
            currencyExchangePairReader.readList(response.body().charStream(), pair -> processPair(pair, syncResult));
        }
    }

    private boolean processPair(final Pair<Currency, Exchange> pair, final SyncResult syncResult) {
        syncResult.stats.numInserts += currencyDao.putCurrencies(pair.first);
        syncResult.stats.numInserts += exchangeDao.putExchanges(pair.first.id, pair.second);
        return Thread.interrupted();
    }
}

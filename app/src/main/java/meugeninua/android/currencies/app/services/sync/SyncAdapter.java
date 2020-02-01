package meugeninua.android.currencies.app.services.sync;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import meugeninua.android.currencies.app.di.AppComponent;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.CurrenciesApp;
import meugeninua.android.currencies.app.di.ComponentInjector;
import meugeninua.android.currencies.model.db.entities.Currency;
import meugeninua.android.currencies.model.db.entities.Exchange;
import meugeninua.android.currencies.model.operations.CurrencyOperations;
import meugeninua.android.currencies.model.operations.ExchangeOperations;
import meugeninua.android.currencies.model.readers.EntityReader;
import meugeninua.android.currencies.model.utils.Utils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SyncAdapter extends AbstractThreadedSyncAdapter implements ComponentInjector {

    private static final String TAG = SyncAdapter.class.getSimpleName();
    private static final String SYNC_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";

    private OkHttpClient httpClient;
    private CurrencyOperations currencyOperations;
    private ExchangeOperations exchangeOperations;
    private EntityReader<Pair<Currency, Exchange>> currencyExchangePairReader;

    SyncAdapter(final Context context) {
        super(context.getApplicationContext(), true);
        CurrenciesApp.inject(this);
    }

    @NonNull
    @Override
    public Context requireContext() {
        return getContext();
    }

    @Override
    public void inject(final AppComponent appComponent) {
        httpClient = appComponent.okHttpClient.get();
        currencyOperations = appComponent.currencyOperations.get();
        exchangeOperations = appComponent.exchangeOperations.get();
        currencyExchangePairReader = appComponent.currencyExchangePairReader.get();
    }

    @Override
    public void onPerformSync(
            final Account account, final Bundle extras, final String authority,
            final ContentProviderClient providerClient, final SyncResult syncResult) {
        try {
            Request request = new Request.Builder()
                    .get().url(SYNC_URL).build();
            List<ContentProviderOperation> operations = processCall(httpClient.newCall(request));
            ContentProviderResult[] results =  providerClient.applyBatch(Utils.toArrayList(operations));
            for (ContentProviderResult result : results) {
                syncResult.stats.numInserts += result.count == null ? 0 : result.count;
            }
            showNotification();
        } catch (IOException e) {
            syncResult.stats.numIoExceptions++;
            Log.e(TAG, e.getMessage(), e);
        } catch (XmlPullParserException e) {
            syncResult.stats.numParseExceptions++;
            Log.e(TAG, e.getMessage(), e);
        } catch (Exception e) {
            syncResult.stats.numAuthExceptions++;
            Log.e(TAG, e.getMessage(), e);
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
                .setSmallIcon(R.drawable.baseline_done_outline_white_18)
                .build();
        manager.notify(0, notification);
    }

    @Nullable
    private List<ContentProviderOperation> processCall(final Call call) throws XmlPullParserException, IOException {
        try (Response response = call.execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return null;
            }
            List<Pair<Currency, Exchange>> pairs = currencyExchangePairReader
                    .readList(response.body().charStream());

            List<ContentProviderOperation> operations = new ArrayList<>();
            for (Pair<Currency, Exchange> pair : pairs) {
                operations.addAll(currencyOperations.putCurrencies(pair.first));
                operations.addAll(exchangeOperations.putExchanges(pair.first.id, pair.second));
            }
            return operations;
        }
    }
}

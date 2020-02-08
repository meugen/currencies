package meugeninua.android.currencies.app.workers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.app.di.Injector;
import meugeninua.android.currencies.app.provider.Constants;
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
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SyncWorker extends Worker implements Injector, Constants {

    private static final String TAG = SyncWorker.class.getSimpleName();
    private static final String SYNC_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";

    private OkHttpClient httpClient;
    private CurrencyOperations currencyOperations;
    private ExchangeOperations exchangeOperations;
    private EntityReader<Pair<Currency, Exchange>> currencyExchangePairReader;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public void inject(AppComponent appComponent) {
        httpClient = appComponent.okHttpClient.get();
        currencyOperations = appComponent.currencyOperations.get();
        exchangeOperations = appComponent.exchangeOperations.get();
        currencyExchangePairReader = appComponent.currencyExchangePairReader.get();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Request request = new Request.Builder()
                    .get().url(SYNC_URL).build();
            List<ContentProviderOperation> operations = processCall(httpClient.newCall(request));
            ContentResolver resolver = getApplicationContext().getContentResolver();
            resolver.applyBatch(AUTHORITY, Utils.toArrayList(operations));
            showNotification();
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return Result.retry();
        }
    }

    private void showNotification() {
        final Context context = getApplicationContext();
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

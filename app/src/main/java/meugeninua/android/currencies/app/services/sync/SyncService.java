package meugeninua.android.currencies.app.services.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SyncService extends Service {

    private static final Object LOCK = new Object();
    private static SyncAdapter syncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (LOCK) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(this);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}

package meugeninua.android.currencies.app.services.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    SyncAdapter(final Context context, final boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(
            final Account account, final Bundle extras, final String authority,
            final ContentProviderClient contentProviderClient, final SyncResult syncResult) {

    }
}

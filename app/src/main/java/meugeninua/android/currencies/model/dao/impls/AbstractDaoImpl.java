package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;

import java.util.List;

import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.data.entity.EntityLiveData;
import meugeninua.android.currencies.model.utils.Utils;

abstract class AbstractDaoImpl implements Constants {

    private final ContentResolver resolver;
    private final Handler workerHandler;

    AbstractDaoImpl(
            final ContentResolver resolver,
            final Handler workerHandler) {
        this.resolver = resolver;
        this.workerHandler = workerHandler;
    }

    EntityLiveData.Builder queryUri(final Uri uri) {
        return new EntityLiveData.Builder()
                .withResolver(resolver)
                .withUri(uri)
                .withWorkerHandler(workerHandler);
    }

    final <T> int putEntities(final List<ContentProviderOperation> operations) {
        try {
            ContentProviderResult[] results = resolver.applyBatch(AUTHORITY,
                    Utils.toArrayList(operations));

            int count = 0;
            for (ContentProviderResult result : results) {
                count += result.count == null ? 0 : result.count;
            }
            return count;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }
}

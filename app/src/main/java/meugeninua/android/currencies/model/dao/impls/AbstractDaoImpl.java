package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Handler;

import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.data.entity.EntityLiveData;
import meugeninua.android.currencies.model.mappers.EntityMapper;

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

    final <T> int putEntities(final Uri uri, final T[] entities, final EntityMapper<T> mapper) {
        ContentValues[] values = new ContentValues[entities.length];
        for (int i = 0; i < entities.length; i++) {
            values[i] = mapper.entityToValues(entities[i]);
        }
        return resolver.bulkInsert(uri, values);
    }
}

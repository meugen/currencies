package meugeninua.android.currencies.model.data.entity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import meugeninua.android.currencies.model.mappers.EntityMapper;

class SingleEntityLiveData<T> extends EntityLiveData<T> {

    private final EntityMapper<T> mapper;

    SingleEntityLiveData(
            final ContentResolver resolver,
            final Uri uri,
            final Handler workerHandler,
            final EntityMapper<T> mapper) {
        super(resolver, uri, workerHandler);
        this.mapper = mapper;
    }

    @Override
    protected T cursorToData(final Cursor cursor) {
        return mapper.cursorToSingleEntity(cursor);
    }
}

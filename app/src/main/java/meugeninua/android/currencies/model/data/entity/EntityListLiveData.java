package meugeninua.android.currencies.model.data.entity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import meugeninua.android.currencies.model.mappers.EntityMapper;

import java.util.List;

class EntityListLiveData<T> extends EntityLiveData<List<T>> {

    private final EntityMapper<T> mapper;

    EntityListLiveData(
            final ContentResolver resolver,
            final Uri uri,
            final Handler workerHandler,
            final EntityMapper<T> mapper) {
        super(resolver, uri, workerHandler);
        this.mapper = mapper;
    }

    @Override
    protected List<T> cursorToData(final Cursor cursor) {
        return mapper.cursorToEntityList(cursor);
    }
}

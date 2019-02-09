package meugeninua.android.currencies.model.dao.impls;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import meugeninua.android.currencies.app.provider.Constants;
import meugeninua.android.currencies.model.mappers.EntityMapper;

abstract class AbstractDaoImpl<T> implements Constants {

    private final ContentResolver resolver;
    private final EntityMapper<T> converter;

    AbstractDaoImpl(final ContentResolver resolver, final EntityMapper<T> converter) {
        this.resolver = resolver;
        this.converter = converter;
    }

    private Cursor queryUri(final Uri uri) {
        return resolver.query(uri, null, null, null, null);
    }

    final List<T> getList(final Uri uri) {
        try (Cursor cursor = queryUri(uri)) {
            List<T> result = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                result.add(converter.cursorToEntity(cursor));
            }
            return result;
        }
    }

    final T getSingle(final Uri uri) {
        try (Cursor cursor = queryUri(uri)) {
            T result = null;
            if (cursor.moveToFirst()) {
                result = converter.cursorToEntity(cursor);
            }
            return result;
        }
    }

    final int putEntities(final Uri uri, final T[] entities) {
        ContentValues[] values = new ContentValues[entities.length];
        for (int i = 0; i < entities.length; i++) {
            values[i] = converter.entityToValues(entities[i]);
        }
        return resolver.bulkInsert(uri, values);
    }
}

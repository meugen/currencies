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

    AbstractDaoImpl(final ContentResolver resolver) {
        this.resolver = resolver;
    }

    Cursor queryUri(final Uri uri) {
        return resolver.query(uri, null, null, null, null);
    }

    final int putEntities(final Uri uri, final T[] entities, final EntityMapper<T> mapper) {
        ContentValues[] values = new ContentValues[entities.length];
        for (int i = 0; i < entities.length; i++) {
            values[i] = mapper.entityToValues(entities[i]);
        }
        return resolver.bulkInsert(uri, values);
    }
}

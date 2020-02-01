package meugeninua.android.currencies.model.mappers.impls;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import meugeninua.android.currencies.model.mappers.EntityMapper;

abstract class AbstractMapper<T> implements EntityMapper<T> {

    @Override
    public final List<T> cursorToEntityList(final Cursor cursor) {
        List<T> result = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            result.add(cursorToEntity(cursor));
        }
        return result;
    }

    @Override
    public T cursorToSingleEntity(Cursor cursor) {
        T result = null;
        if (cursor.moveToFirst()) {
            result = cursorToEntity(cursor);
        }
        return result;
    }
}

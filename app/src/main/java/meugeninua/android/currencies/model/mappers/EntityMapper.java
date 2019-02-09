package meugeninua.android.currencies.model.mappers;

import android.content.ContentValues;
import android.database.Cursor;

public interface EntityMapper<T> {

    T cursorToEntity(Cursor cursor);

    ContentValues entityToValues(T entity);
}

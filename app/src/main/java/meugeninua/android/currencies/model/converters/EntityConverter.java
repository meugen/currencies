package meugeninua.android.currencies.model.converters;

import android.content.ContentValues;
import android.database.Cursor;

public interface EntityConverter<T> {

    T cursorToEntity(Cursor cursor);

    ContentValues entityToValues(T entity);
}

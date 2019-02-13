package meugeninua.android.currencies.model.mappers;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

public interface EntityMapper<T> {

    T cursorToEntity(Cursor cursor);

    List<T> cursorToEntityList(Cursor cursor);

    T cursorToSingleEntity(Cursor cursor);

    ContentValues entityToValues(T entity);
}

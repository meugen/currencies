package meugeninua.android.currencies.model.mappers.utils;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class MapperUtils {

    private MapperUtils() { }

    public static List<String> cursorToStringList(final Cursor cursor, final String fldName) {
        List<String> result = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndexOrThrow(fldName)));
        }
        return result;
    }
}

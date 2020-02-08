package meugeninua.android.currencies.model.data.entity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import meugeninua.android.currencies.model.mappers.utils.MapperUtils;

import java.util.List;

class StringListLiveData extends EntityLiveData<List<String>> {

    private final String fldName;

    StringListLiveData(
            final ContentResolver resolver,
            final Uri uri,
            final Handler workerHandler,
            final String fldName) {
        super(resolver, uri, workerHandler);
        this.fldName = fldName;
    }

    @Override
    protected List<String> cursorToData(final Cursor cursor) {
        return MapperUtils.cursorToStringList(cursor, fldName);
    }
}

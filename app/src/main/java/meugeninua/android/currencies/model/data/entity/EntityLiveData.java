package meugeninua.android.currencies.model.data.entity;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import meugeninua.android.currencies.model.data.utils.Clearable;
import meugeninua.android.currencies.model.mappers.EntityMapper;

public abstract class EntityLiveData<T> extends LiveData<T> implements Clearable {

    private final ContentResolver resolver;
    private final Uri uri;
    private final Handler workerHandler;

    private volatile Cursor cursor;

    EntityLiveData(
            final ContentResolver resolver,
            final Uri uri,
            final Handler workerHandler) {
        if (workerHandler.getLooper() == Looper.getMainLooper()) {
            throw new IllegalArgumentException("Handler must be worker handler, not main one");
        }
        this.resolver = resolver;
        this.uri = uri;
        this.workerHandler = workerHandler;
    }

    protected abstract T cursorToData(final Cursor cursor);

    @WorkerThread
    private void loadData() {
        Log.d(getClass().getSimpleName(), "loadData() method called, uri = " + uri);
        if (cursor != null) {
            cursor.close();
        }
        cursor = resolver.query(uri, null, null, null, null);
        cursor.registerContentObserver(new CursorContentObserver(this));
        postValue(cursorToData(cursor));
    }

    @Override
    protected void onActive() {
        if (cursor == null) {
            workerHandler.post(this::loadData);
        }
    }

    @Override
    public void clear() {
        Log.d(getClass().getSimpleName(), "clear() method called, uri = " + uri);
        if (cursor !=  null) {
            cursor.close();
            cursor = null;
        }
    }

    private static class CursorContentObserver extends ContentObserver {

        private final WeakReference<EntityLiveData> liveDataRef;

        CursorContentObserver(final EntityLiveData liveData) {
            super(liveData.workerHandler);
            liveDataRef = new WeakReference<>(liveData);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(final boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(final boolean selfChange, final Uri uri) {
            EntityLiveData liveData = liveDataRef.get();
            if (liveData == null) {
                return;
            }
            liveData.loadData();
        }
    }

    public static class Builder {

        private ContentResolver resolver;
        private Uri uri;
        private Handler workerHandler;

        public Builder withResolver(final ContentResolver resolver) {
            this.resolver = resolver;
            return this;
        }

        public Builder withUri(final Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder withWorkerHandler(final Handler workerHandler) {
            this.workerHandler = workerHandler;
            return this;
        }

        public <T> LiveData<T> buildSingleEntityLiveData(final EntityMapper<T> mapper) {
            return new SingleEntityLiveData<>(resolver,
                    uri, workerHandler, mapper);
        }

        public <T> LiveData<List<T>> buildEntityListLiveData(final EntityMapper<T> mapper) {
            return new EntityListLiveData<T>(resolver,
                    uri, workerHandler, mapper);
        }

        public LiveData<List<String>> buildStringListLiveData(final String fldName) {
            return new StringListLiveData(resolver, uri, workerHandler, fldName);
        }
    }
}

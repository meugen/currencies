package meugeninua.android.currencies.ui.fragments.base.binding;

import android.util.SparseArray;
import android.view.View;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseBinding implements Binding {

    private WeakReference<View> rootViewRef;
    private SparseArray<WeakReference<View>> childrenViewRefs;

    @Override
    public final void attachView(final View view) {
        rootViewRef = new WeakReference<>(view);
        childrenViewRefs = new SparseArray<>();
    }

    @Override
    public final void detachView() {
        rootViewRef = null;
        childrenViewRefs = null;
    }

    @Nullable
    private <V extends View> V getNullable(final int id) {
        if (rootViewRef == null || childrenViewRefs == null) {
            return null;
        }
        WeakReference<View> childViewRef = childrenViewRefs.get(id);
        V childView = childViewRef == null ? null : (V) childViewRef.get();
        if (childView != null) {
            return childView;
        }
        View rootView = rootViewRef == null
                ? null : rootViewRef.get();
        if (rootView == null) {
            return null;
        }
        childView = rootView.findViewById(id);
        if (childView != null) {
            childrenViewRefs.put(id, new WeakReference<>(childView));
        }
        return childView;
    }

    @NonNull
    @Override
    public final <V extends View> V get(final int id) {
        V childView = getNullable(id);
        if (childView == null) {
            throw new RuntimeException("View with id " + id + " not found.");
        }
        return childView;
    }

    @Override
    public final boolean has(final int id) {
        return getNullable(id) != null;
    }
}

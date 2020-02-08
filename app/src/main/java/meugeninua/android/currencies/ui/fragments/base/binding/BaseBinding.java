package meugeninua.android.currencies.ui.fragments.base.binding;

import android.util.SparseArray;
import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.*;

import java.lang.ref.WeakReference;

public abstract class BaseBinding implements Binding, LifecycleObserver, Observer<LifecycleOwner> {

    private final Fragment fragment;
    private WeakReference<View> rootViewRef;
    private SparseArray<WeakReference<View>> childrenViewRefs;

    protected BaseBinding(final Fragment fragment) {
        this.fragment = fragment;
        fragment.getViewLifecycleOwnerLiveData()
                .observeForever(this);
    }

    @Override
    public final void onChanged(final LifecycleOwner lifecycleOwner) {
        onAttachView(fragment.getView());
        lifecycleOwner.getLifecycle().addObserver(this);
        fragment.getViewLifecycleOwnerLiveData().removeObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public final void onDestroyView() {
        onDetachView();
    }

    @CallSuper
    protected void onAttachView(final View view) {
        rootViewRef = new WeakReference<>(view);
        childrenViewRefs = new SparseArray<>();
    }

    @CallSuper
    protected void onDetachView() {
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

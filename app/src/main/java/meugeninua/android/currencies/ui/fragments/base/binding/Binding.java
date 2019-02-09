package meugeninua.android.currencies.ui.fragments.base.binding;

import android.view.View;

import androidx.annotation.NonNull;

public interface Binding {

    void attachView(View view);

    void detachView();

    @NonNull
    <V extends View> V get(int id);

    boolean has(int id);
}

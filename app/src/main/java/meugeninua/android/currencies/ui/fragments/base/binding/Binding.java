package meugeninua.android.currencies.ui.fragments.base.binding;

import android.view.View;

import androidx.annotation.NonNull;

public interface Binding {

    @NonNull
    <V extends View> V get(int id);

    boolean has(int id);
}

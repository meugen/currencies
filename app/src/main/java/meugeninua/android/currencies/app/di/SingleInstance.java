package meugeninua.android.currencies.app.di;

import androidx.annotation.NonNull;

public interface SingleInstance<T> {

    @NonNull
    T get();
}

interface InstanceFactory<T> {

    @NonNull
    T create();
}

class LazySingleInstance<T> implements SingleInstance<T> {

    private InstanceFactory<T> factory;
    private T instance;

    LazySingleInstance(@NonNull InstanceFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    @NonNull
    public T get() {
        if (instance == null) {
            instance = factory.create();
            factory = null;
        }
        return instance;
    }
}

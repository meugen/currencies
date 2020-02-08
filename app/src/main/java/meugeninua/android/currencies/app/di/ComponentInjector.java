package meugeninua.android.currencies.app.di;

import android.content.Context;
import androidx.annotation.NonNull;

public interface ComponentInjector extends Injector {

    @NonNull
    Context requireContext();
}

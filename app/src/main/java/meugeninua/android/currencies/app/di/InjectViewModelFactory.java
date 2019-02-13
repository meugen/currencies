package meugeninua.android.currencies.app.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class InjectViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppComponent appComponent;

    InjectViewModelFactory(final AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull final Class<T> modelClass) {
        T viewModel = super.create(modelClass);
        if (viewModel instanceof Injector) {
            ((Injector) viewModel).inject(appComponent);
        }
        return viewModel;
    }
}

package meugeninua.android.currencies.ui.fragments.base.utils;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

public class FragmentUtils {

    private FragmentUtils() { }

    public static <VM extends ViewModel> VM getViewModel(
            final Fragment fragment,
            final ViewModelProvider.Factory factory,
            final Class<VM> clazz) {
        return ViewModelProviders.of(fragment, factory).get(clazz);
    }
}

package meugeninua.android.currencies.ui.fragments.base.presenter;

import meugeninua.android.currencies.ui.fragments.base.state.MvpState;
import meugeninua.android.currencies.ui.fragments.base.view.MvpView;

public interface MvpPresenter<V extends MvpView, S extends MvpState> {

    void restoreState(S state);

    void saveState(S state);
}

package meugeninua.android.currencies.ui.activities.base.fragments.base.presenter;

import meugeninua.android.currencies.ui.activities.base.fragments.base.state.MvpState;
import meugeninua.android.currencies.ui.activities.base.fragments.base.view.MvpView;

public interface MvpPresenter<V extends MvpView, S extends MvpState> {

    void restoreState(S state);

    void saveState(S state);
}

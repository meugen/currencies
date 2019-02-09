package meugeninua.android.currencies.ui.fragments.base.state;

import android.os.Bundle;

public interface MvpState {

    void attachBundle(Bundle bundle);

    void detachBundle();

    class EmptyState extends BaseState {}
}

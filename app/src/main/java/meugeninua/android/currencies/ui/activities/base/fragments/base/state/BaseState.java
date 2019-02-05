package meugeninua.android.currencies.ui.activities.base.fragments.base.state;

import android.os.Bundle;

public class BaseState implements MvpState {

    protected Bundle bundle;

    @Override
    public void attachBundle(final Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void detachBundle() {
        this.bundle = null;
    }
}

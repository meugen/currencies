package meugeninua.android.currencies.ui.fragments.message.binding;

import android.widget.TextView;

import meugeninua.android.currencies.R;
import meugeninua.android.currencies.ui.fragments.base.binding.BaseBinding;

public class MessageBindingImpl extends BaseBinding implements MessageBinding {

    @Override
    public void setMessage(final int resId) {
        this.<TextView>get(R.id.message).setText(resId);
    }
}

package meugeninua.android.currencies.ui.fragments.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import meugeninua.android.currencies.R;
import meugeninua.android.currencies.app.di.AppComponent;
import meugeninua.android.currencies.ui.fragments.base.BaseFragment;
import meugeninua.android.currencies.ui.fragments.message.binding.MessageBinding;
import meugeninua.android.currencies.ui.fragments.message.binding.MessageBindingImpl;

public class MessageFragment extends BaseFragment<MessageBinding> {

    private static final String ARG_MESSAGE_ID = "message_id";

    public static MessageFragment build(final int messageId) {
        Bundle args = new Bundle();
        args.putInt(ARG_MESSAGE_ID, messageId);

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setMessage(getArguments().getInt(ARG_MESSAGE_ID));
    }

    @Override
    protected void inject(final AppComponent appComponent) {
        super.inject(appComponent);
        this.binding = new MessageBindingImpl();
    }
}

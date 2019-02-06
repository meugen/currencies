package meugeninua.android.currencies.app.services.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AuthenticatorService extends Service {

    private Authenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        this.authenticator = new Authenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return authenticator.getIBinder();
    }
}

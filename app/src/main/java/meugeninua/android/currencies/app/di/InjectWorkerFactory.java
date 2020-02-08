package meugeninua.android.currencies.app.di;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.ObjectsCompat;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;
import meugeninua.android.currencies.app.workers.SyncWorker;

class InjectWorkerFactory extends WorkerFactory {

    private final AppComponent component;

    InjectWorkerFactory(AppComponent component) {
        this.component = component;
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(
            @NonNull Context appContext,
            @NonNull String workerClassName,
            @NonNull WorkerParameters workerParameters
    ) {
        ListenableWorker worker = null;
        if (ObjectsCompat.equals(workerClassName, SyncWorker.class.getName())) {
            worker = new SyncWorker(appContext, workerParameters);
        }

        if (worker instanceof Injector) {
            ((Injector) worker).inject(component);
        }
        return worker;
    }
}

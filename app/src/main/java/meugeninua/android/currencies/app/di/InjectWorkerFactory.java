package meugeninua.android.currencies.app.di;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;
import meugeninua.android.currencies.app.workers.SyncWorker;

import java.util.HashMap;
import java.util.Map;

class InjectWorkerFactory extends WorkerFactory {

    private final AppComponent component;
    private final Map<String, WorkerCreator<?>> creators;

    InjectWorkerFactory(AppComponent component) {
        this.component = component;

        creators = new HashMap<>();
        creators.put(SyncWorker.class.getName(), SyncWorker::new);
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(
            @NonNull Context appContext,
            @NonNull String workerClassName,
            @NonNull WorkerParameters workerParameters
    ) {
        WorkerCreator<?> creator = creators.get(workerClassName);
        ListenableWorker worker = creator == null ? null
                : creator.create(appContext, workerParameters);

        if (worker instanceof Injector) {
            ((Injector) worker).inject(component);
        }
        return worker;
    }

    private interface WorkerCreator<W extends ListenableWorker> {

        W create(Context context, WorkerParameters parameters);
    }
}

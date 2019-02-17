package meugeninua.android.currencies.model.data.utils;

import androidx.lifecycle.LiveData;

public class LiveDataUtils {

    private LiveDataUtils() { }

    public static void clearLiveDataIfNeeded(final LiveData<?>... datas) {
        for (LiveData<?> data : datas) {
            if (data instanceof Clearable) {
                ((Clearable) data).clear();
            }
        }
    }
}

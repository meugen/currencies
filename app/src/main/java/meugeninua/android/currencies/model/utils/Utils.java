package meugeninua.android.currencies.model.utils;

import java.util.ArrayList;
import java.util.Collection;

public class Utils {

    private Utils() {}

    public static <T> ArrayList<T> toArrayList(final Collection<T> collection) {
        if (collection instanceof ArrayList) {
            return (ArrayList<T>) collection;
        }
        return new ArrayList<>(collection);
    }
}

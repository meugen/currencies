package meugeninua.android.currencies.model.db.entities;

import androidx.core.util.ObjectsCompat;

public class Currency {

    public int id;
    public String name;
    public String code;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Currency currency = (Currency) o;
        return id == currency.id &&
                ObjectsCompat.equals(name, currency.name) &&
                ObjectsCompat.equals(code, currency.code);
    }

    @Override
    public int hashCode() {
        return ObjectsCompat.hash(id, name, code);
    }
}

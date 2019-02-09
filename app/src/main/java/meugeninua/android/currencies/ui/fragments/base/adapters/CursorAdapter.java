package meugeninua.android.currencies.ui.fragments.base.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import meugeninua.android.currencies.model.mappers.EntityMapper;

public abstract class CursorAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final LayoutInflater inflater;
    private final EntityMapper<T> converter;
    private final AsyncListDiffer<T> differ;

    private List<T> items = Collections.emptyList();

    protected CursorAdapter(
            final Context context,
            final EntityMapper<T> converter,
            final DiffUtil.ItemCallback<T> callback) {
        this.inflater = LayoutInflater.from(context);
        this.converter = converter;
        this.differ = new AsyncListDiffer<>(this, callback);
    }

    public final void swapCursor(final Cursor cursor) {
        this.items = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            this.items.add(converter.cursorToEntity(cursor));
        }
        differ.submitList(this.items);
    }

    protected abstract VH createViewHolder(final LayoutInflater inflater, final ViewGroup parent);

    protected abstract void bindViewHolder(final VH holder, final T entity);

    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return createViewHolder(inflater, parent);
    }

    @Override
    public final void onBindViewHolder(@NonNull final VH holder, final int position) {
        bindViewHolder(holder, items.get(position));
    }

    @Override
    public final int getItemCount() {
        return items.size();
    }
}

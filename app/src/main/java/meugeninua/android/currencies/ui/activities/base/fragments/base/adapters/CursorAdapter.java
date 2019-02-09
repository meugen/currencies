package meugeninua.android.currencies.ui.activities.base.fragments.base.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import meugeninua.android.currencies.model.mappers.EntityMapper;

public abstract class CursorAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final LayoutInflater inflater;
    private final EntityMapper<T> converter;

    private Cursor cursor;

    protected CursorAdapter(final Context context, final EntityMapper<T> converter) {
        this.inflater = LayoutInflater.from(context);
        this.converter = converter;
    }

    public final void swapCursor(final Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
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
        cursor.moveToPosition(position);
        bindViewHolder(holder, converter.cursorToEntity(cursor));
    }

    @Override
    public final int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }
}

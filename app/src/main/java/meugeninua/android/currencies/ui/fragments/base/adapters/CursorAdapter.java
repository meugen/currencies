package meugeninua.android.currencies.ui.fragments.base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class CursorAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final LayoutInflater inflater;
    private final AsyncListDiffer<T> differ;

    protected CursorAdapter(
            final Context context,
            final DiffUtil.ItemCallback<T> callback) {
        this.inflater = LayoutInflater.from(context);
        this.differ = new AsyncListDiffer<>(this, callback);
    }

    public final void swapContent(final List<T> items) {
        differ.submitList(items);
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
        bindViewHolder(holder, differ.getCurrentList().get(position));
    }

    @Override
    public final int getItemCount() {
        return differ.getCurrentList().size();
    }
}

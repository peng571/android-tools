package dev.momo.library.core.data.recycler;

/**
 * Connect long click event with adapter and viewHolder
 * for control adapter item change or activity ui change when viewHolder click
 * <p>
 * Created by momo peng on 2016/12/29.
 */

public interface OnItemLongClickListener<T> {

    // On hold view holder click
    void onItemLongClick(ObjectViewHolder viewHolder, int position, T object);

}

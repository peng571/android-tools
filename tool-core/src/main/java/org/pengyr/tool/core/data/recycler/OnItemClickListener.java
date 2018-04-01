package org.pengyr.tool.core.data.recycler;

/**
 * Connect click event with adapter and viewHolder
 * for control adapter item change or activity ui change when viewHolder click
 * <p>
 * Created by momo peng on 2016/12/29.
 */

public interface OnItemClickListener<T> {

    // On hold view holder click
    void onItemClick(ObjectViewHolder viewHolder, int position, T object);

}

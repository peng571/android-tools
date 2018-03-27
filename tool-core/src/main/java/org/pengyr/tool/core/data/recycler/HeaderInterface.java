package org.pengyr.tool.core.data.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * Implement this if need to create a recyclerAdapter with headerView
 * @see HeaderRecyclerAdapter for example
 *
 * @param <T> type of Header
 */
public interface HeaderInterface<T extends RecyclerView.ViewHolder> {

    int HEAD_TYPE = 1;
    int ITEM_TYPE = 2;

    /**
     * @param position in list
     * @return if position is header position
     */
    boolean isHead(int position);

}

package org.pengyr.tool.core.data.recycler;

import android.support.v7.widget.RecyclerView;

/**
 * Example class of ListRecyclerAdapter with Header
 * if need to extends from RefreshAdapter or other
 * use current adapter and implements HeaderInterface, and fix method below with header
 *
 * @see HeaderInterface
 *
 * @param <VH> type of view holder
 * @param <T>  type of item in adapter
 */
abstract class HeaderRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends ListRecyclerAdapter<T, VH> implements HeaderInterface<VH> {

    @Override
    public boolean isHead(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHead(position)) return HEAD_TYPE;
        return ITEM_TYPE;
    }

}

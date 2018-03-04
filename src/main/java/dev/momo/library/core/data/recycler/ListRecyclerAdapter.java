package dev.momo.library.core.data.recycler;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.momo.library.core.log.Logger;

/**
 * Created by momopeng on 3/17/16.
 */
public abstract class ListRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final static String TAG = ListRecyclerAdapter.class.getSimpleName();

    protected List<T> data;

    protected ArrayMap<Integer, OnItemClickListener> clickListener;
    protected ArrayMap<Integer, OnItemLongClickListener> longClickListener;


    public ListRecyclerAdapter() {
        this(new ArrayList<T>());
    }

    public ListRecyclerAdapter(List<T> list) {
        setData(list);
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyItemRangeChanged(0, data.size());
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public List<T> getData() {
        return data;
    }


    public T getItem(int position) {
        if (data == null) return null;
        int index = fixSafeIndex(position);
        return data.get(index);
    }

    public int indexOf(T item) {
        return data.indexOf(item);
    }

    public void add(T t) {
        if (t == null) return;
        if (data.contains(t)) return;
        data.add(t);
        notifyItemInserted(getItemCount() - 1);
    }


    /**
     * Fix method name to 'addWithIndex' to avoid confused when add an Integer Object into array
     *
     * @param t     type
     * @param index in array
     */
    public void addWithIndex(T t, int index) {
        if (t == null) return;
        if (data.contains(t)) {
            remove(t);
        }
        index = fixSafeIndex(index);
        data.add(index, t);
        notifyItemInserted(index);
    }


    public void addAll(Collection<T> collection) {
        if (collection == null) return;
        for (T t : collection) {
            add(t);
        }
    }


    public void addAll(T[] collection) {
        if (collection == null) return;
        for (T t : collection) {
            add(t);
        }
    }

    public void remove(T t) {
        int position = data.indexOf(t);
        if (position < 0) return;
        data.remove(t);
        notifyItemRemoved(position);
    }

    public void removeIndex(int position) {
        int p = fixSafeIndex(position);
        data.remove(p);
        notifyItemRemoved(p);
    }

    public void editIndex(T t, int postition) {
        int p = fixSafeIndex(postition);
        data.set(p, t);
        notifyItemChanged(p);
    }


    public void clear() {
        int range = getItemCount();
        if (data != null) {
            data.clear();
        }
        notifyItemRangeRemoved(0, range);
    }

    public boolean isEmpty() {
        if (data == null) return true;
        return data.isEmpty();
    }

    private int fixSafeIndex(int index) {
        if (data.isEmpty()) return 0;
        if (index < 0) {
            Logger.ES(TAG, "get index %d less then 0", index);
            return 0;
        } else if (index >= data.size()) {
            Logger.ES(TAG, "get index %d large then item size %d", index, getItemCount());
            return data.size() - 1;
        }
        return index;
    }


    public void setItemClickListener(OnItemClickListener<T> listener) {
        if (clickListener == null) {
            clickListener = new ArrayMap<>();
        }
        clickListener.put(0, listener);
    }


    public void addItemClickListener(int viewType, OnItemClickListener<T> listener) {
        if (clickListener == null) {
            clickListener = new ArrayMap<>();
        }
        clickListener.put(viewType, listener);
    }

    public void setItemLongClickListener(OnItemLongClickListener<T> listener) {
        if (longClickListener == null) {
            longClickListener = new ArrayMap<>();
        }
        longClickListener.put(0, listener);
    }

    public void addItemLongClickListener(int viewType, OnItemLongClickListener<T> listener) {
        if (longClickListener == null) {
            longClickListener = new ArrayMap<>();
        }
        longClickListener.put(viewType, listener);
    }


}
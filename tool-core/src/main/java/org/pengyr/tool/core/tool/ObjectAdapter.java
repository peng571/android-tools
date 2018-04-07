package org.pengyr.tool.core.tool;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Rewrite from android.widget.ArrayAdapter<T>
 *
 * @param <T> type to save in adapter
 */
public abstract class ObjectAdapter<T> extends BaseAdapter {

    protected ArrayList<T> data = new ArrayList<>();

    public ObjectAdapter() {
        super();
    }

    public void setData(ArrayList<T> data) {
        if (data == null) {
            return;
        }

        this.data = data;
        this.notifyDataSetChanged();
    }

    public ArrayList<T> getData() {
        return data;
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= data.size() || position < 0) {
            return null;
        }
        return data.get(position);
    }

    @Override
    @Deprecated
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public abstract View getView(int position, View rowView, ViewGroup parent);

    public void add(T t) {
        if (data.contains(t)) {
            return;
        }
        data.add(t);
        notifyDataSetChanged();
    }

    public void add(T t, int index) {
        if (data.contains(t)) {
            data.remove(t);
            return;
        }
        data.add(index, t);
        notifyDataSetChanged();

    }


    public void addAll(Collection<T> collection) {
        for (T t : collection) {
            add(t);
        }
    }


    public void addAll(T[] collection) {
        for (T t : collection) {
            add(t);
        }
    }

    public void remove(T t) {
        data.remove(t);
        notifyDataSetChanged();
    }


    @Deprecated
    /**
     * Confused on using T as Integer, Avoid remove from index
     */
    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }


    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void clear() {
        if (data != null) {
            data.clear();
        }
        notifyDataSetChanged();
    }

}

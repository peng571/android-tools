package dev.momo.library.core.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.momo.library.core.log.Logger;

/**
 * to keep change data for adapter
 * <p>
 * only refresh data when adapter is on main ui thread
 * <p>
 * Created by Peng on 2017/6/13.
 */
public class DataProvider<D> {

    private final static String TAG = DataProvider.class.getSimpleName();

    @NonNull
    protected List<D> data;

    @NonNull
    protected Set<D> notifyIDs;

    @Nullable
    protected PositionFixer positionFixer;

    @Nullable
    protected RecyclerView.Adapter<?> adapter;

    public DataProvider() {
        data = new ArrayList<>();
        notifyIDs = new HashSet<>();
    }

    public void addNotifiID(@NonNull D data) {
        if (adapter != null) return;
        notifyIDs.add(data);
    }

    public boolean has(@NonNull D d) {
        return find(d) >= 0;
    }

    public int find(@NonNull D d) {
        return data.indexOf(d);
    }

    @Nullable
    public D get(int position) {
        if (position < 0) return null;
        if (position >= count()) return null;
        return data.get(position);
    }

    /**
     * add Methods
     */
    public synchronized void add(@NonNull D model) {
        if (adapter != null) {
            addCurrent(model, count());
            return;
        }
        notifyIDs.add(model);
    }

    public synchronized void addAll(@NonNull D[] datas) {
        for (D d : datas) {
            add(d);
        }
    }

    public synchronized void addAll(@NonNull Collection<D> datas) {
        for (D d : datas) {
            add(d);
        }
    }


    /**
     * remove methods
     */
    public synchronized void remove(@NonNull D data) {
        if (adapter != null) {
            removeCurrent(data);
            return;
        }
        addNotifiID(data);
    }


    public synchronized void remove(@NonNull List<D> datas) {
        for (D d : datas) {
            remove(d);
        }
    }


    /**
     * update methods
     */
    public synchronized void update(@NonNull D id) {
        if (adapter != null) {
            updateCurrent(id);
            return;
        }
        addNotifiID(id);
    }


    /**
     * Bind adapter to refresh ui when data change
     * Must bind(null) when adapter view is not shown
     */
    public void unbind() {
        this.adapter = null;
    }

    public void bind(@NonNull RecyclerView.Adapter<?> adapter) {
        bind(adapter, null);
    }

    public void bind(@NonNull RecyclerView.Adapter<?> adapter, @Nullable OnNotifyListener<D> onNotifyListener) {
        this.adapter = adapter;
        if (onNotifyListener != null) {
            onNotifyListener.onNotify(notifyIDs);
            notifyIDs.clear();
        }
    }

    protected int fixAdapterPosition(int position) {
        if (positionFixer == null) return position;
        return positionFixer.getFixPosition(position);
    }

    public void setPositionFixer(@NonNull PositionFixer positionFixer) {
        this.positionFixer = positionFixer;
    }

    /**
     * update current data only when adapter is exist
     * must run on main ui thread
     *
     * @param id    update value
     * @param index update index (if need)
     */
    @UiThread
    public synchronized void addCurrent(@NonNull D id, int index) {
        if (adapter == null) return;
        if (data.indexOf(id) >= 0) {
            removeCurrent(id);
            index--;
        }
        if (index < 0) index = 0;
        if (index > count()) index = count();

        data.add(index, id);
        adapter.notifyItemInserted(fixAdapterPosition(index));
    }

    @UiThread
    public synchronized void removeCurrent(@NonNull D d) {
        if (adapter == null) return;

        int index = data.indexOf(d);
        if (index < 0) return;
        if (index >= count()) return;
        data.remove(index);
        adapter.notifyItemRemoved(fixAdapterPosition(index));
    }

    @UiThread
    public synchronized void updateCurrent(@NonNull D d) {
        if (adapter == null) return;

        int index = data.indexOf(d);
        if (index < 0) return;
        if (index >= count()) return;
        adapter.notifyItemChanged(fixAdapterPosition(index));
    }


    /**
     * pump out all notifyIDs into dataIDs
     *
     * @return all data in list
     */
    public List<D> pumpList() {
        for (D d : notifyIDs) {
            if (!data.contains(d)) {
                data.add(d);
            }
        }
        return data;
    }


    /**
     * @return current data
     */
    public List<D> list() {
        return data;
    }

    /**
     * @return notify data
     */
    public Set<D> getNotifyIDs() {
        return notifyIDs;
    }

    public int count() {
        return data.size();
    }

    public boolean isEmpty() {
        return count() == 0;
    }


    public void clear() {
        if (adapter == null) return;
        data.clear();
        adapter.notifyDataSetChanged();
    }

    public void destroy() {
        unbind();
        positionFixer = null;
        adapter = null;
        data.clear();
        notifyIDs.clear();
    }


    public interface PositionFixer {
        int getFixPosition(int position);
    }

    public interface OnNotifyListener<P> {
        void onNotify(Set<P> notifyIDs);
    }
}

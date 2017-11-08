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

/**
 * to keep change data for adapter
 * <p>
 * only refresh data when adapter is on main ui thread
 * <p>
 * Created by Peng on 2017/6/13.
 */
public class DataModelProvider<M extends DataModel<P>, P> extends DataProvider<P> {

    private final static String TAG = DataModelProvider.class.getSimpleName();

    public DataModelProvider() {
        super();
    }


    /**
     * add Methods
     */
    public void add(@NonNull M model) {
        super.add(model.getID());
    }

    public void addAll(@NonNull M[] models) {
        for (M m : models) {
            add(m);
        }
    }


    /**
     * remove methods
     */
    public void remove(@NonNull M m) {
        remove(m.getID());
    }


    /**
     * update methods
     */
    public void update(@NonNull M m) {
        update(m.getID());
    }

}

package org.pengyr.tool.core.data;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

import java.util.List;

import org.pengyr.tool.core.log.Logger;

/**
 * Base DataManager
 *
 * @param <P> Type of Key
 * @param <T> Type of Object must extends DataItem<P>
 */
@TargetApi(19)
public class DataManager<P, T extends DataModel<P>> {

    private final static String TAG = DataManager.class.getSimpleName();

    public static DataManager instance = null;

    protected ArrayMap<P, T> objectDict; // post id : PostObject

    @TargetApi(19)
    protected DataManager() {
        objectDict = new ArrayMap<>();
    }

    public synchronized static DataManager get() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }


    public synchronized boolean hasObject(P ID) {
        if (ID == null) return false;
        return objectDict.containsKey(ID);
    }


    public synchronized T getObject(@NonNull P ID) {
        if (objectDict.containsKey(ID)) {
            return objectDict.get(ID);
        }
        Logger.WS(TAG, "can't find Object in ID: %s", String.valueOf(ID));
        return null;
    }


    public synchronized void addObject(@NonNull T object) {
        object.getID();
        objectDict.put(object.getID(), object);
    }


    public synchronized void addObjects(@NonNull List<T> objects) {
        for (T t : objects) {
            addObject(t);
        }
    }

    public synchronized void deleteObject(@NonNull P ID) {
        objectDict.remove(ID);
    }

    public void clear() {
        objectDict.clear();
        instance = null;
    }
}



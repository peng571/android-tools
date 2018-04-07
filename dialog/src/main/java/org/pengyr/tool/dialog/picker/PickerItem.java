package org.pengyr.tool.dialog.picker;

import android.support.annotation.Nullable;

public class PickerItem<T> {

    @Nullable
    private T object;

    private String name;
    private PickerCallback<T> callback;


    public PickerItem(String name, PickerCallback<T> callback) throws ClassNotFoundException {
        this.object = null;
        this.name = name;
        this.callback = callback;
    }

    public PickerItem(T object, PickerOption<T> option, PickerCallback<T> callback) {
        this.object = object;
        this.name = option.getName(object);
        this.callback = callback;
    }

    public void click() {
        callback.onPick(0, getValue());
    }


    public String getName() {
        return name;
    }

    public T getValue() {
        return object;
    }


    public PickerCallback getCallback() {
        return callback;
    }

    public interface PickerOption<T> {
        String getName(T t);
    }

}

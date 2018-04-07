package org.pengyr.tool.dialog.picker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;

import org.pengyr.tool.dialog.BaseDialog;
import org.pengyr.tool.dialog.DialogConstants;

import java.util.ArrayList;


/**
 * DialogFragment with picker option
 * <p>
 * Usage
 * on activity: implement dialog interface to handle
 * on fragment: implement dialog interface to handle, and add setTargetFragment as dialog
 * <p>
 * Event Handle
 * - Handle pick event with PickerCallback in PickerItem
 * - Handle cancel event with DialogFinishHolder.onDialogCancel
 * - Handle dismiss event with DialogFinishHolder.onDialogDismiss
 */
public class PickerDialog extends BaseDialog {

    private static final String TAG = PickerDialog.class.getSimpleName();

    protected ArrayList<PickerItem<?>> items;

    public static PickerDialog newInstance() {
        return newInstance(0, 0);
    }

    public static PickerDialog newInstance(@StringRes int titleRes, int requset) {
        PickerDialog f = new PickerDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(DialogConstants.KEY_TITLE_RES, titleRes);
        args.putInt(DialogConstants.KEY_REQUEST, requset);
        f.setArguments(args);
        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ArrayList<PickerItem<?>> items = getItems();
        String[] itemNames = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).getName();
        }

        int titleRes = getArguments().getInt(DialogConstants.KEY_TITLE_RES, 0);
        if (titleRes != 0) {
            builder.setTitle(titleRes);
        }
        request = getArguments().getInt(DialogConstants.KEY_REQUEST);

        builder.setItems(itemNames, (DialogInterface dialog, int which) -> {
            PickerItem item = getItems().get(which);
            PickerCallback callback = item.getCallback();
            if (callback == null) return;
            callback.onPick(which, item.getValue());

        });
        return builder.create();
    }


    public PickerDialog addItem(PickerItem item) {
        getItems().add(item);
        return this;
    }


    public PickerDialog addItem(String name, PickerCallback<String> callback) throws ClassNotFoundException {
        getItems().add(new PickerItem<String>(name, callback));
        return this;
    }

    public <T> PickerDialog addItem(T object, PickerItem.PickerOption<T> option, PickerCallback<T> callback) {
        getItems().add(new PickerItem<T>(object, option, callback));
        return this;
    }


    protected ArrayList<PickerItem<?>> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public PickerItem<?> getItem(int position) {
        if (position < 0 || position >= items.size()) return null;
        return items.get(position);
    }

    public int getCount() {
        return getItems().size();
    }

    @Override
    protected int getRequestCode() {
        Bundle bundle = getArguments();
        return bundle.getInt(DialogConstants.KEY_REQUEST, request);
    }

    @Override
    protected String getTagName() {
        return TAG;
    }
}


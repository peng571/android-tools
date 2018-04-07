package org.pengyr.tool.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;


/**
 * DialogFragment with one ok button and simple message
 * <p>
 * Usage
 * on activity: implement dialog interface to handle
 * on fragment: implement dialog interface to handle, and add setTargetFragment as dialog
 * <p>
 * Event Handle
 * - Handle ok event with DialogYesHolder
 * - Handle cancel event with DialogFinishHolder.onDialogCancel
 * - Handle dismiss event with DialogFinishHolder.onDialogDismiss
 */
public class OKDialog extends BaseDialog {

    private final static String TAG = OKDialog.class.getSimpleName();

    public static OKDialog newInstance(@StringRes int messageRes, @StringRes int yesRes, int request) {
        OKDialog f = new OKDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(DialogConstants.KEY_MESSAGE_RES, messageRes);
        args.putInt(DialogConstants.KEY_YES_RES, yesRes);
        args.putInt(DialogConstants.KEY_REQUEST, request);
        f.setArguments(args);
        return f;
    }

    public static OKDialog newInstance(@StringRes int messageRes, int request) {
        return newInstance(messageRes, R.string.dialog_ok, request);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        int titleRes = bundle.getInt(DialogConstants.KEY_TITLE_RES, 0);
        int messageRes = bundle.getInt(DialogConstants.KEY_MESSAGE_RES, 0);
        int iconRes = bundle.getInt(DialogConstants.KEY_ICON_DRAWABLE, 0);
        int yesRes = bundle.getInt(DialogConstants.KEY_YES_RES, R.string.dialog_ok);
        request = bundle.getInt(DialogConstants.KEY_REQUEST);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (titleRes != 0) {
            builder.setTitle(titleRes);
        }
        if (messageRes != 0) {
            builder.setMessage(messageRes);
        }
        if (iconRes != 0) {
            builder.setIcon(iconRes);
        }


        DialogInterface.OnClickListener dialogClickListener = (DialogInterface dialog, int which) -> onNext();
        builder.setPositiveButton(yesRes, dialogClickListener);
        return builder.create();
    }

    @Override
    protected int getRequestCode() {
        Bundle args = new Bundle();
        return args.getInt(DialogConstants.KEY_REQUEST, request);
    }

    @Override
    protected String getTagName() {
        return TAG;
    }
}

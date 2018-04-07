package org.pengyr.tool.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * DialogFragment with two button and simple message
 * <p>
 * Usage
 * on activity: implement dialog interface to handle
 * on fragment: implement dialog interface to handle, and add setTargetFragment as dialog
 * (fragment will have higher priority then activity)
 * <p>
 * Event Handle
 * - Handle yes (positive) event with DialogYesHolder
 * - Handle no (negative) event with DialogNoHolder
 * - Handle cancel event with DialogFinishHolder.onDialogCancel or DialogNoHolder(only when not find DialogFinishHolder),
 * - Handle dismiss event with DialogFinishHolder.onDialogDismiss
 * <p>
 * Created on 2016/12/12.
 * Updated on 2017/4/27.
 * by momo peng
 */
public class YesNoDialog extends BaseDialog {

    private final static String TAG = YesNoDialog.class.getSimpleName();

    /**
     * Create a new instance with argument.
     */
    public static YesNoDialog newInstance(@StringRes int messageRes, @StringRes int yesRes, @StringRes int noRes, int requset) {
        YesNoDialog f = new YesNoDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(DialogConstants.KEY_MESSAGE_RES, messageRes);
        args.putInt(DialogConstants.KEY_YES_RES, yesRes);
        args.putInt(DialogConstants.KEY_NO_RES, noRes);
        args.putInt(DialogConstants.KEY_REQUEST, requset);
        f.setArguments(args);
        return f;
    }

    public static YesNoDialog newInstance(@StringRes int messageRes, @StringRes int yesRes, int request) {
        return newInstance(messageRes, yesRes, R.string.dialog_no, request);
    }

    public static YesNoDialog newInstance(@StringRes int messageRes, int requset) {
        return newInstance(messageRes, R.string.dialog_yes, requset);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        int titleRes = bundle.getInt(DialogConstants.KEY_TITLE_RES, 0);
        int messageRes = bundle.getInt(DialogConstants.KEY_MESSAGE_RES, 0);
        int iconRes = bundle.getInt(DialogConstants.KEY_ICON_DRAWABLE, 0);
        int yesRes = bundle.getInt(DialogConstants.KEY_YES_RES, R.string.dialog_yes);
        int noRes = bundle.getInt(DialogConstants.KEY_NO_RES, R.string.dialog_no);
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


        DialogInterface.OnClickListener dialogClickListener = (DialogInterface dialog, int which) -> {
            if (getActivity() == null) return;
            switch (which) {
                case BUTTON_POSITIVE:
                    onNext();
                    break;
                case BUTTON_NEGATIVE:
                    onCancel();
                    break;
            }
        };

        builder.setPositiveButton(yesRes, dialogClickListener).setNegativeButton(noRes, dialogClickListener);
        return builder.create();
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

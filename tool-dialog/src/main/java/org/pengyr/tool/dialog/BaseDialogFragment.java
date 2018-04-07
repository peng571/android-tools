package org.pengyr.tool.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import org.pengyr.tool.dialog.holder.DialogYesHolder;


/**
 * a base dialog for custom dialog, and with shown and dismiss animator
 * <p>
 * For example.
 * Create a new instance with argument.
 * <p>
 * '''
 * private static BaseDialogFragment newInstance() {
 * BaseDialogFragment f = new BaseDialogFragment() {
 *
 * @Override protected int getWidth() {
 * return MATCH_PARENT;
 * }
 * @Override protected int getHeight() {
 * return WRAP_CONTENT;
 * }
 * @Override protected int getRequestCode() {
 * return 0;
 * }
 * };
 * Bundle args = new Bundle();
 * args.putString("key", "value");
 * f.setArguments(args);
 * return f;
 * }
 * '''
 * <p>
 * Created by momo peng on 2016/12/12.
 */
public abstract class BaseDialogFragment extends BaseDialog
        implements DialogInterface.OnShowListener {

    private final static String TAG = BaseDialogFragment.class.getSimpleName();


    /**
     * The system calls this only when creating the layout in a dialog.
     * Should not be call from other class
     * Call newInstance to create dialog instead.
     */
    @Override
    @Deprecated
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Do not override this method, unless needed to modify any dialog characteristics.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnShowListener(this);
        return dialog;
    }


    protected void onNext() {
        if (getActivity() == null) return;

        DialogYesHolder holder = null;
        if (getActivity() instanceof DialogYesHolder)
            holder = (DialogYesHolder) getActivity();
        if (getTargetFragment() instanceof DialogYesHolder)
            holder = (DialogYesHolder) getTargetFragment();

        if (holder != null) {
            holder.doOnDialogYesClick(getRequestCode());
        }
        dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();

        // resize dialog to full screen
        Dialog dialog = getDialog();
        if (dialog == null) return;

        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(getWidth(), getHeight());
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // show pop-on anim
        final View decorView = window.getDecorView();
        if (decorView == null) return;
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                decorView,
                PropertyValuesHolder.ofFloat("scaleX", 0f, 1.0f),
                PropertyValuesHolder.ofFloat("scaleY", 0f, 1.0f),
                PropertyValuesHolder.ofFloat("alpha", 0f, 1.0f));
        scaleDown.setDuration(250);
        scaleDown.start();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getDialog() == null) return;
        if (getDialog().getWindow() == null) return;
        if (getDialog().getWindow().getDecorView() == null) return;

        // show pop-off anim
        final View decorView = getDialog()
                .getWindow()
                .getDecorView();

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                decorView,
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0f),
                PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0f),
                PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f));
        scaleDown.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BaseDialogFragment.super.dismiss();
            }

            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        scaleDown.setDuration(200);
        scaleDown.start();
    }

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected abstract int getRequestCode();

}
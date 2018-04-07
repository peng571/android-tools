package dev.momo.library.view.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by momo peng on 2018/3/13.
 */

public class DialogHolderActivity extends Activity implements
        DialogYesHolder,
        DialogFinishHolder {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public void doOnDialogYesClick(int request) {

    }

    @Override public void doOnDialogCancel(int request) {

    }

    @Override public void doOnDialogDismiss(int request) {

    }
}

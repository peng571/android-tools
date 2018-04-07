package org.pengyr.tool.dialog.holder;

/**
 * Created by Peng on 2017/4/3.
 */

public interface DialogFinishHolder {

    /**
     * Will be call when dialog been canceled
     * happen when
     * - user press back button
     * - click range out of dialog
     *
     * @param request dialog request
     */
    void doOnDialogCancel(int request);

    /**
     * Will be call when dialog dismiss
     *
     * @param request dialog request
     */
    void doOnDialogDismiss(int request);

}

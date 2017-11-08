package dev.momo.library.core.ui;

/**
 * Work with UIController
 *
 * Use to send event to the container
 * Draw self when need
 */
public interface UIListener<D> {

    void setUIController(UIController controller);

    void onUIDraw(int eventCode, D data);

}

package dev.momo.library.core.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Work with UIListener
 * <p/>
 * To get the event from sub UIView ( who will implement UIListener.)
 * And can send back some information about drawing UI.
 */
public abstract class UIController<E> {

    List<UIListener<E>> listeners = new ArrayList<>();

    /**
     * Implement a List of UIListeners when have more then one UIListener
     *
     * @param listener
     */
    void registerUIListener(UIListener listener){
        listeners.add(listener);
    }

    void unregisterUIListener(UIListener listener){
        listeners.remove(listener);
    }

    void unregisterAllUIListener(){
        listeners.clear();
    }

    /**
     * Call by UIListener to send some event on UIListener
     *
     * @param event integer code of custom by developer
     * @param data result data with type T
     */
    abstract void onUISend(int event, E data);

}

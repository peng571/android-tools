package dev.momo.library.connect.data.parser;

import dev.momo.library.core.data.DataModel;

/**
 * An implemented class of ObjectRefresher
 *
 * @param <T> object type that extends from DataModel, parse P as ID
 */
public class ModelObjectParser<P, T extends DataModel<P>> extends ObjectParser<P, T> {

    public P parse(T t) {
        return t.getID();
    }

}

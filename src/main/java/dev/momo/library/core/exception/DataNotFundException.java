package dev.momo.library.core.exception;

import java.util.Locale;

/**
 * Created by Peng on 2016/6/12.
 */
public class DataNotFundException extends Exception {

    private final static String exceptionMessage = "Data [%s]:%s not found";

    public DataNotFundException(Class type, String id) {
        super(String.format(Locale.getDefault(), exceptionMessage, type.getName(), id));
    }

    public DataNotFundException(Class type, int id) {
        this(type, String.valueOf(id));
    }

    public DataNotFundException(Class type, long id) {
        this(type, String.valueOf(id));
    }
}
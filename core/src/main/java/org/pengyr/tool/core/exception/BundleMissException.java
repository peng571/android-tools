package org.pengyr.tool.core.exception;

/**
 * Created by Peng on 2016/6/12.
 */
public class BundleMissException extends Exception {

    private final static String exceptionMessage = "Lose Bundle [%s]";

    public BundleMissException(String missExtra) {
        super(String.format(exceptionMessage, missExtra));
    }

}
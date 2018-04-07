package org.pengyr.tool.core.exception;

/**
 * Created by Peng on 2017/4/4.
 */

public class InaccurateDataException extends Exception {

    private final static String exceptionMessage = "Data with inaccurate value [%s]: %s";

    public InaccurateDataException(String inaccurateKey, String inaccurateValue) {
        super(String.format(exceptionMessage, inaccurateKey, inaccurateValue));
    }

    public InaccurateDataException(String inaccurateKey, int inaccurateValue) {
        this(inaccurateKey, String.valueOf(inaccurateValue));
    }
}

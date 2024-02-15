package com.carbon.deliverytracker.utils.logging;

public class ErrorUtils {
    public static String getErrorCause(Throwable e) {
        return e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
    }
}

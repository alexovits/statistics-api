package com.alexovits.stats.api.exception;

/**
 * Created by zsoltszabo on 8/8/17.
 */
public class InvalidTransactionException extends RuntimeException {
    public final String message;

    public InvalidTransactionException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

package com.alexovits.stats.api.exception;

import java.time.Instant;

/**
 * Created by zsoltszabo on 8/8/17.
 */
public class InvalidTransactionException extends Exception {
    public final String message;
    public Instant timestamp;

    public InvalidTransactionException(String message) {
        this.message = message;
    }

    public InvalidTransactionException(String message, Instant timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp(){
        return timestamp;
    }
}

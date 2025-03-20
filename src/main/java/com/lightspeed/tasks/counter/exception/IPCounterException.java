package com.lightspeed.tasks.counter.exception;

public class IPCounterException extends RuntimeException {

    public IPCounterException(String message) {
        super(message);
    }

    public IPCounterException(String message, Throwable cause) {
        super(message, cause);
    }
}

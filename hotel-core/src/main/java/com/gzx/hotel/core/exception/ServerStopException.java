package com.gzx.hotel.core.exception;

public class ServerStopException extends Exception{

    public ServerStopException() {
    }

    public ServerStopException(String message) {
        super(message);
    }
}

package com.epam.ak.model.parser;

public class PavilionHandlerException extends RuntimeException {
    public PavilionHandlerException() {
    }

    public PavilionHandlerException(String message) {
        super(message);
    }

    public PavilionHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PavilionHandlerException(Throwable cause) {
        super(cause);
    }
}

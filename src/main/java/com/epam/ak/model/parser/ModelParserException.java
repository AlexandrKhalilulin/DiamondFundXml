package com.epam.ak.model.parser;

public class ModelParserException extends RuntimeException {

    public ModelParserException() {
    }

    public ModelParserException(String message) {
        super(message);
    }

    public ModelParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelParserException(Throwable cause) {
        super(cause);
    }
}

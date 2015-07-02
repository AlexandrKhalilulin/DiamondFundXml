package com.epam.ak.model.parser;

public class ModelParserHandlerException extends RuntimeException {
    public ModelParserHandlerException() {
    }

    public ModelParserHandlerException(String message) {
        super(message);
    }

    public ModelParserHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelParserHandlerException(Throwable cause) {
        super(cause);
    }
}

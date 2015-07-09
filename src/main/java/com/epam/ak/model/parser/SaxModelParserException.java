package com.epam.ak.model.parser;

public class SaxModelParserException extends RuntimeException {

    public SaxModelParserException() {
    }

    public SaxModelParserException(String message) {
        super(message);
    }

    public SaxModelParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaxModelParserException(Throwable cause) {
        super(cause);
    }
}

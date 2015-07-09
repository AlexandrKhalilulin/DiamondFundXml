package com.epam.ak.model.Runner;

public class RunnerException extends RuntimeException {
    public RunnerException() {
    }

    public RunnerException(String message) {
        super(message);
    }

    public RunnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RunnerException(Throwable cause) {
        super(cause);
    }
}

package com.example.demo.exception;

public class DataProcessionException extends RuntimeException {
    public DataProcessionException(String message, Throwable cause) {
        super(message, cause);
    }
}

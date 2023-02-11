package com.rs.elasticsearchservice.exception;

public class BadApiKeyException extends RuntimeException {
    public BadApiKeyException(String message) {
        super(message);
    }
}

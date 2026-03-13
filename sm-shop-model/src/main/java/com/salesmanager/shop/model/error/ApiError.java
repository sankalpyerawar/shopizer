package com.salesmanager.shop.model.error;

import java.time.Instant;

/**
 * Java 17+ Record for API error responses.
 * Immutable, compact, and type-safe.
 */
public record ApiError(
    int status,
    String message,
    String path,
    Instant timestamp
) {
    public ApiError {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("Invalid HTTP status code: " + status);
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Error message cannot be null or blank");
        }
    }
    
    public static ApiError of(int status, String message, String path) {
        return new ApiError(status, message, path, Instant.now());
    }
}

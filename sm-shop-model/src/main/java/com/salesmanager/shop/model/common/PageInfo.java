package com.salesmanager.shop.model.common;

/**
 * Java 17+ Record for pagination metadata.
 * Immutable value object for page information.
 */
public record PageInfo(
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    public PageInfo {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("Total elements cannot be negative");
        }
    }
    
    public boolean hasNext() {
        return page < totalPages - 1;
    }
    
    public boolean hasPrevious() {
        return page > 0;
    }
}

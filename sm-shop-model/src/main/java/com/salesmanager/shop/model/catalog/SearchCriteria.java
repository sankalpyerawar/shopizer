package com.salesmanager.shop.model.catalog;

/**
 * Java 17+ Record for product search criteria.
 * Immutable value object for search parameters.
 */
public record SearchCriteria(
    String query,
    String category,
    String manufacturer,
    Double minPrice,
    Double maxPrice
) {
    public SearchCriteria {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Min price cannot be greater than max price");
        }
    }
    
    public boolean hasQuery() {
        return query != null && !query.isBlank();
    }
    
    public boolean hasPriceRange() {
        return minPrice != null || maxPrice != null;
    }
}

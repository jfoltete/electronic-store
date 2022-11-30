package com.jf.electronicstore.cart.domain.model;

public class ProductId {

    private final Long id;

    public ProductId(Long id) {
        this.id = id;
    }

    public static ProductId from(Long productId) {
        return new ProductId(productId);
    }

    public Long getId() {
        return id;
    }
}

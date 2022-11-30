package com.jf.electronicstore.cart.domain.model;

public class CartId {
    private final Long id;

    public CartId(Long id) {
        this.id = id;
    }

    public static CartId from(Long cartId) {
        return new CartId(cartId);
    }

    public Long getId() {
        return id;
    }
}

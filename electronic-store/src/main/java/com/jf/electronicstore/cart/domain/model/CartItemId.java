package com.jf.electronicstore.cart.domain.model;

public class CartItemId {
    private final Long id;

    public CartItemId(Long id) {
        this.id = id;
    }

    public static CartItemId from(Long cartItemId) {
        return new CartItemId(cartItemId);
    }

    public Long getId() {
        return id;
    }
}

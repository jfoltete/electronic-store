package com.jf.electronicstore.cart.application.dto;

import com.jf.electronicstore.cart.domain.model.CartItem;

public record CartItemDTO(Long id, Long productId, Integer quantity) {

    public static CartItemDTO from(CartItem cartItem) {
        return new CartItemDTO(cartItem.getId(), cartItem.getProduct().getId(), cartItem.getQuantity());
    }
}

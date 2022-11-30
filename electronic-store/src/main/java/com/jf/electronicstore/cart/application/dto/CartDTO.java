package com.jf.electronicstore.cart.application.dto;

import com.jf.electronicstore.cart.domain.model.Cart;

import java.util.List;

public record CartDTO(Long id, List<CartItemDTO> items) {

    public static CartDTO from(Cart cart) {
        return new CartDTO(cart.getId(), cart.getCartItems().stream().map(CartItemDTO::from).toList());
    }
}

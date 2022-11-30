package com.jf.electronicstore.cart.domain.factory;

import com.jf.electronicstore.cart.domain.model.Cart;
import com.jf.electronicstore.cart.domain.model.CartId;
import com.jf.electronicstore.cart.domain.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CartFactory {
    private final CartRepository cartRepository;

    @Autowired
    public CartFactory(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart retrieve(CartId cartId) {
        return cartRepository.findById(cartId.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
    }
}

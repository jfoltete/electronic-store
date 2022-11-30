package com.jf.electronicstore.cart.domain.factory;

import com.jf.electronicstore.cart.domain.model.Cart;
import com.jf.electronicstore.cart.domain.model.CartItem;
import com.jf.electronicstore.cart.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ItemFactory {
    public CartItem create(Cart cart, Product product, int quantity) {
        return new CartItem(product, quantity, cart);
    }
}

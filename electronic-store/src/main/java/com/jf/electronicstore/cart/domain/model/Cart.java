package com.jf.electronicstore.cart.domain.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart() {}

    public Cart(Long id, List<CartItem> cartItems) {
        this.id = id;
        this.cartItems = cartItems;
    }

    public Long getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void add(CartItem cartItem) {
        if (contains(cartItem.getProduct().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This product is already in the cart");
        }
        cartItems.add(cartItem);
    }

    public void delete(CartItemId cartItemId) {
        cartItems.removeIf(item -> item.getId().equals(cartItemId.getId()));
    }

    private boolean contains(Long productId) {
        return cartItems.stream().anyMatch(item -> item.getProduct().getId().equals(productId));
    }
}

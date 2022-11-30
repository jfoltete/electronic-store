package com.jf.electronicstore.cart.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;

    @JsonIgnore
    @JoinColumn(name = "cart_id")
    @ManyToOne
    private Cart cart;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    private CartItem() {}

    public CartItem(Product product, Integer quantity, Cart cart) {
        this.product = product;
        this.quantity = quantity;
        this.cart = cart;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Cart getCart() {
        return cart;
    }

    public Product getProduct() {
        return product;
    }
}

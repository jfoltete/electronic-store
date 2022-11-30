package com.jf.electronicstore.cart.domain.model;

import javax.persistence.*;

@Entity
@Table
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer targetQuantity;
    private Integer discountPercent;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    public Coupon() {}

    public Coupon(Product product, Integer targetQuantity, Integer discountPercent) {
        this.targetQuantity = targetQuantity;
        this.discountPercent = discountPercent;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Integer getTargetQuantity() {
        return targetQuantity;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }
}

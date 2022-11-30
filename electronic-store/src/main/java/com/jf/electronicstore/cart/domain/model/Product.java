package com.jf.electronicstore.cart.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer availability;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Coupon> coupons = new ArrayList<>();

    public Product() {}

    public Product(String name, BigDecimal price, int availability) {
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public Product(Long id, String name, BigDecimal price, Integer availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getAvailability() {
        return availability;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void add(Coupon coupon) {
        coupons.add(coupon);
    }
}

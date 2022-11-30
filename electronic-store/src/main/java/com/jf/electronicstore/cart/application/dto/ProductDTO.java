package com.jf.electronicstore.cart.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jf.electronicstore.cart.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductDTO {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Integer availability;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<CouponDTO> coupons;

    public ProductDTO(Long id, String name, BigDecimal price, Integer availability, List<CouponDTO> coupons) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
        this.coupons = coupons;
    }

    public static ProductDTO from(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getAvailability(),
                product.getCoupons().stream().map(CouponDTO::from).toList());
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

    public List<CouponDTO> getCoupons() {
        return coupons;
    }
}

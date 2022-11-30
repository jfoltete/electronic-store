package com.jf.electronicstore.cart.application.dto;

import com.jf.electronicstore.cart.domain.model.Coupon;

public record CouponDTO(Long id, Integer targetQuantity, Integer discountPercent) {

    public static CouponDTO from(Coupon coupon) {
        return new CouponDTO(coupon.getId(), coupon.getTargetQuantity(), coupon.getDiscountPercent());
    }
}

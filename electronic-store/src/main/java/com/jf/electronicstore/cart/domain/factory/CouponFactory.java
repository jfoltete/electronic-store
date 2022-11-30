package com.jf.electronicstore.cart.domain.factory;

import com.jf.electronicstore.cart.domain.model.Coupon;
import com.jf.electronicstore.cart.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class CouponFactory {
    public Coupon create(Product product, Integer targetQuantity, Integer discountPercent) {
        return new Coupon(product, targetQuantity, discountPercent);
    }

}

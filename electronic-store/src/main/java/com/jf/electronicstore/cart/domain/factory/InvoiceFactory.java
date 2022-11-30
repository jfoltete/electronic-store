package com.jf.electronicstore.cart.domain.factory;

import com.jf.electronicstore.cart.application.dto.CartItemDTO;
import com.jf.electronicstore.cart.application.dto.CouponDTO;
import com.jf.electronicstore.cart.application.dto.InvoiceDTO;
import com.jf.electronicstore.cart.domain.model.Cart;
import com.jf.electronicstore.cart.domain.model.CartItem;
import com.jf.electronicstore.cart.domain.model.Coupon;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InvoiceFactory {
    private final Map<Long, List<CouponDTO>> discountApplied = new HashMap<>();

    public InvoiceDTO create(Cart cart) {
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal totalDiscount = new BigDecimal(0);

        for (CartItem cartItem : cart.getCartItems()) {
            BigDecimal itemPrice = computeItemPrice(cartItem.getProduct().getPrice(), cartItem.getQuantity());
            BigDecimal itemDiscount = computeItemDiscount(cartItem);

            totalPrice = totalPrice.add(itemPrice);
            totalDiscount = totalDiscount.add(itemDiscount);
        }

        totalPrice = totalPrice.setScale(2, RoundingMode.DOWN);
        totalDiscount = totalDiscount.setScale(2, RoundingMode.DOWN);

        List<CartItemDTO> items = cart.getCartItems().stream().map(CartItemDTO::from).toList();

        return new InvoiceDTO(totalPrice, totalDiscount, items, discountApplied);
    }

    private BigDecimal computeItemDiscount(CartItem cartItem) {
        BigDecimal itemDiscount = new BigDecimal(0);
        for (Coupon coupon : cartItem.getProduct().getCoupons()) {
            BigDecimal couponDiscount = computeCouponDiscount(cartItem, coupon);

            itemDiscount = itemDiscount.add(couponDiscount);
        }
        return itemDiscount;
    }

    private BigDecimal computeCouponDiscount(CartItem cartItem, Coupon coupon) {
        BigDecimal couponDiscount = new BigDecimal(0);
        int targetQuantity = coupon.getTargetQuantity();
        int itemQuantity = cartItem.getQuantity();

        if (itemQuantity >= targetQuantity) {
            addToDiscountApplied(cartItem, coupon);

            int discountedItemsCount = countDiscountedItems(targetQuantity, itemQuantity);
            BigDecimal itemPrice = cartItem.getProduct().getPrice();
            BigDecimal discountedItemsTotalPrice = computeItemPrice(itemPrice, discountedItemsCount);
            BigDecimal discountPercent = computeDiscountPercent(coupon.getDiscountPercent());

            couponDiscount = applyDiscount(discountedItemsTotalPrice, discountPercent);
        }

        return couponDiscount;
    }

    private static int countDiscountedItems(int targetQuantity, int itemQuantity) {
        return itemQuantity / targetQuantity;
    }

    private static BigDecimal computeItemPrice(BigDecimal price, int count) {
        return new BigDecimal(count).multiply(price);
    }

    private static BigDecimal computeDiscountPercent(Integer discountPercent) {
        return new BigDecimal(discountPercent).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
    }

    private static BigDecimal applyDiscount(BigDecimal totalPrice, BigDecimal discountPercent) {
        return totalPrice.multiply(discountPercent);
    }

    private void addToDiscountApplied(CartItem cartItem, Coupon coupon) {
        Long productId = cartItem.getProduct().getId();
        discountApplied.computeIfAbsent(productId, p -> new ArrayList<>());
        discountApplied.get(productId).add(CouponDTO.from(coupon));
    }
}

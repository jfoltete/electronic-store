package com.jf.electronicstore.cart.domain.factory;

import com.jf.electronicstore.cart.application.dto.CartItemDTO;
import com.jf.electronicstore.cart.application.dto.CouponDTO;
import com.jf.electronicstore.cart.application.dto.InvoiceDTO;
import com.jf.electronicstore.cart.domain.model.Cart;
import com.jf.electronicstore.cart.domain.model.CartItem;
import com.jf.electronicstore.cart.domain.model.Coupon;
import com.jf.electronicstore.cart.domain.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceFactoryTest {

    @Test
    void invoice_without_discount() {
        Product product_1 = new Product(1L, "product1", new BigDecimal("20.20"), 3);
        Product product_2 = new Product(2L, "product2", new BigDecimal("10.10"), 3);
        Product product_3 = new Product(3L, "product2", new BigDecimal("30.30"), 3);

        CartItem cartItem_1 = new CartItem(product_1, 2, null);
        CartItem cartItem_2 = new CartItem(product_2, 2, null);
        CartItem cartItem_3 = new CartItem(product_3, 1, null);

        InvoiceFactory invoiceFactory = new InvoiceFactory();

        List<CartItem> cartItems = List.of(cartItem_1, cartItem_2, cartItem_3);
        Cart cart = new Cart(1L, cartItems);

        InvoiceDTO invoice = invoiceFactory.create(cart);

        BigDecimal expectedTotalPrice = new BigDecimal("90.90"); // (2 * 20.20) + (2 * 10.10) + (1 * 30.30)
        BigDecimal expectedTotalDiscount = new BigDecimal("0.00");
        List<CartItemDTO> expectedItems = cartItems.stream().map(CartItemDTO::from).toList();
        Map<Long, List<CouponDTO>> expectedDiscountsApplied = Collections.emptyMap();
        InvoiceDTO expectedInvoice = new InvoiceDTO(expectedTotalPrice, expectedTotalDiscount, expectedItems, expectedDiscountsApplied);

        assertEquals(expectedInvoice, invoice);
    }

    @Test
    void invoice_with_discount() {
        Coupon coupon_1 = new Coupon(null, 2, 50);
        Coupon coupon_2 = new Coupon(null, 1, 10);
        Coupon coupon_3 = new Coupon(null, 5, 30);

        Product product_1 = new Product(1L, "product1", new BigDecimal("20.20"), 3);
        Product product_2 = new Product(2L, "product2", new BigDecimal("10.10"), 3);
        Product product_3 = new Product(3L, "product2", new BigDecimal("30.30"), 3);

        product_1.add(coupon_1);
        product_2.add(coupon_2);
        product_3.add(coupon_3);

        CartItem cartItem_1 = new CartItem(product_1, 2, null);
        CartItem cartItem_2 = new CartItem(product_2, 2, null);
        CartItem cartItem_3 = new CartItem(product_3, 1, null);

        InvoiceFactory invoiceFactory = new InvoiceFactory();

        List<CartItem> cartItems = List.of(cartItem_1, cartItem_2, cartItem_3);
        Cart cart = new Cart(1L, cartItems);

        InvoiceDTO invoice = invoiceFactory.create(cart);

        BigDecimal expectedTotalPrice = new BigDecimal("90.90"); // (2 * 20.20) + (2 * 10.10) + (1 * 30.30)
        BigDecimal expectedTotalDiscount = new BigDecimal("12.12"); // (1 * 20.20 * 50%) + (2 * 10.10 * 10%)
        List<CartItemDTO> expectedItems = cartItems.stream().map(CartItemDTO::from).toList();
        Map<Long, List<CouponDTO>> expectedDiscountsApplied = Map.of(1L, List.of(CouponDTO.from(coupon_1)), 2L, List.of(CouponDTO.from(coupon_2)));
        InvoiceDTO expectedInvoice = new InvoiceDTO(expectedTotalPrice, expectedTotalDiscount, expectedItems, expectedDiscountsApplied);

        assertEquals(expectedInvoice, invoice);
    }

    @Test
    void empty_invoice() {
        InvoiceFactory invoiceFactory = new InvoiceFactory();

        List<CartItem> cartItems = Collections.emptyList();
        Cart cart = new Cart(1L, cartItems);

        InvoiceDTO invoice = invoiceFactory.create(cart);

        BigDecimal expectedTotalPrice = new BigDecimal("00.00");
        BigDecimal expectedTotalDiscount = new BigDecimal("0.00");
        List<CartItemDTO> expectedItems = Collections.emptyList();
        Map<Long, List<CouponDTO>> expectedDiscountsApplied = Collections.emptyMap();
        InvoiceDTO expectedInvoice = new InvoiceDTO(expectedTotalPrice, expectedTotalDiscount, expectedItems, expectedDiscountsApplied);

        assertEquals(expectedInvoice, invoice);
    }


}
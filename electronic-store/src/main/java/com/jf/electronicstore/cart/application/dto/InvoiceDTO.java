package com.jf.electronicstore.cart.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record InvoiceDTO(BigDecimal totalPrice,
                         BigDecimal totalDiscount,
                         List<CartItemDTO> items,
                         Map<Long, List<CouponDTO>> discountsApplied) {}

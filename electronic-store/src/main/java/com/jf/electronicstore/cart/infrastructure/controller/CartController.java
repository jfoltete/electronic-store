package com.jf.electronicstore.cart.infrastructure.controller;

import com.jf.electronicstore.cart.application.dto.CartDTO;
import com.jf.electronicstore.cart.application.dto.CartItemDTO;
import com.jf.electronicstore.cart.application.dto.InvoiceDTO;
import com.jf.electronicstore.cart.application.service.CartApplicationService;
import com.jf.electronicstore.cart.domain.model.CartId;
import com.jf.electronicstore.cart.domain.model.CartItemId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Controller
public class CartController {
    public static final String CARTS_MAPPING = "/carts";
    public static final String CARTS_ITEMS_MAPPING = CARTS_MAPPING + "/{cartId}/items";
    public static final String INVOICE_MAPPING = CARTS_MAPPING + "/{cartId}/invoice";

    @Autowired
    CartApplicationService cartApplicationService;

    @PostMapping(value = CARTS_MAPPING)
    public ResponseEntity<CartDTO> addCart() {

        CartDTO cart = cartApplicationService.addCart();

        return ResponseEntity.created(generateLocation(cart.id())).body(cart);
    }

    @GetMapping(value = CARTS_MAPPING + "/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long cartId) {

        CartDTO cart = cartApplicationService.getCart(CartId.from(cartId));

        return ResponseEntity.ok(cart);
    }

    @GetMapping(value = CARTS_ITEMS_MAPPING)
    public ResponseEntity<List<CartItemDTO>> getCartItems(@PathVariable Long cartId) {

        List<CartItemDTO> cartItems = cartApplicationService.getCartItems(CartId.from(cartId));

        return ResponseEntity.ok(cartItems);
    }

    @PostMapping(value = CARTS_ITEMS_MAPPING)
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long cartId,
                                                  @RequestBody CartItemDTO cartItemDTO) {

        CartDTO updatedCart = cartApplicationService.addItem(CartId.from(cartId), cartItemDTO);

        return ResponseEntity.created(generateLocation(updatedCart.id())).body(updatedCart);
    }

    @DeleteMapping(value = CARTS_ITEMS_MAPPING + "/{cartItemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long cartId,
                                                   @PathVariable Long cartItemId) {

        cartApplicationService.deleteItem(CartId.from(cartId), CartItemId.from(cartItemId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = INVOICE_MAPPING)
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable Long cartId) {

        InvoiceDTO invoice = cartApplicationService.getInvoice(CartId.from(cartId));

        return ResponseEntity.ok(invoice);
    }

    private static URI generateLocation(Long id) {
        return ServletUriComponentsBuilder
                .fromPath(CARTS_MAPPING + "/{cartId}")
                .buildAndExpand(id)
                .toUri();
    }

}

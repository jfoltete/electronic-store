package com.jf.electronicstore.cart.domain.factory;

import com.jf.electronicstore.cart.domain.model.Cart;
import com.jf.electronicstore.cart.domain.model.CartId;
import com.jf.electronicstore.cart.domain.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class CartFactoryTest {

    @Mock
    private CartRepository cartRepository;

    @Test
    void retrieve_cart() {
        Cart cart = new Cart(1L, Collections.emptyList());
        Mockito.when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart retrievedCart = new CartFactory(cartRepository).retrieve(new CartId(1L));

        assertEquals(cart, retrievedCart);
    }

    @Test
    void throws_when_cart_not_found() {
        Mockito.when(cartRepository.findById(2L)).thenReturn(Optional.empty());

        try {
            new CartFactory(cartRepository).retrieve(new CartId(2L));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }
}
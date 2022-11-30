package com.jf.electronicstore.cart.application.service;

import com.jf.electronicstore.cart.application.dto.CartDTO;
import com.jf.electronicstore.cart.application.dto.CartItemDTO;
import com.jf.electronicstore.cart.application.dto.InvoiceDTO;
import com.jf.electronicstore.cart.domain.factory.CartFactory;
import com.jf.electronicstore.cart.domain.factory.InvoiceFactory;
import com.jf.electronicstore.cart.domain.factory.ItemFactory;
import com.jf.electronicstore.cart.domain.factory.ProductFactory;
import com.jf.electronicstore.cart.domain.model.*;
import com.jf.electronicstore.cart.domain.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CartApplicationService {
    private final CartRepository cartRepository;
    private final ItemFactory itemFactory;
    private final CartFactory cartFactory;
    private final ProductFactory productFactory;

    @Autowired
    public CartApplicationService(CartRepository cartRepository, ItemFactory itemFactory, CartFactory cartFactory, ProductFactory productFactory) {
        this.cartRepository = cartRepository;
        this.itemFactory = itemFactory;
        this.cartFactory = cartFactory;
        this.productFactory = productFactory;
    }

    public CartDTO getCart(CartId cartId) {
        return CartDTO.from(cartFactory.retrieve(cartId));
    }

    public CartDTO addCart() {
        return CartDTO.from(cartRepository.save(new Cart()));
    }

    public List<CartItemDTO> getCartItems(CartId cartId) {
        Cart cart = cartFactory.retrieve(cartId);

        return cart.getCartItems().stream().map(CartItemDTO::from).toList();
    }

    public CartDTO addItem(CartId cartId, CartItemDTO cartItemDTO) {
        Cart cart = cartFactory.retrieve(cartId);
        Product product = productFactory.retrieve(ProductId.from(cartItemDTO.productId()));
        CartItem cartItem = itemFactory.create(cart, product, cartItemDTO.quantity());

        cart.add(cartItem);
        cartRepository.flush();

        return CartDTO.from(cart);
    }

    public void deleteItem(CartId cartId, CartItemId cartItemId) {
        Cart cart = cartFactory.retrieve(cartId);

        cart.delete(cartItemId);
        cartRepository.flush();
    }

    public InvoiceDTO getInvoice(CartId cartId) {
        Cart cart = cartFactory.retrieve(cartId);

        return new InvoiceFactory().create(cart);
    }
}

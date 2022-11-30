package com.jf.electronicstore.cart.domain.factory;

import com.jf.electronicstore.cart.domain.model.Product;
import com.jf.electronicstore.cart.domain.model.ProductId;
import com.jf.electronicstore.cart.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ProductFactory {
    private final ProductRepository productRepository;

    @Autowired
    public ProductFactory(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product retrieve(ProductId productId) {
        return productRepository.findById(productId.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}

package com.jf.electronicstore.cart.domain.factory;

import com.jf.electronicstore.cart.domain.model.Product;
import com.jf.electronicstore.cart.domain.model.ProductId;
import com.jf.electronicstore.cart.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class ProductFactoryTest {

    @Mock
    private ProductRepository productRepository;


    @Test
    void retrieve_product() {
        Product product = new Product();
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product retrievedProduct = new ProductFactory(productRepository).retrieve(new ProductId(1L));

        assertEquals(product, retrievedProduct);
    }

    @Test
    void throws_when_product_not_found() {
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.empty());

        try {
            new ProductFactory(productRepository).retrieve(new ProductId(2L));
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }
}
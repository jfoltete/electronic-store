package com.jf.electronicstore.cart.infrastructure.controller;

import com.jf.electronicstore.cart.application.dto.CouponDTO;
import com.jf.electronicstore.cart.application.dto.ProductDTO;
import com.jf.electronicstore.cart.application.service.ProductApplicationService;
import com.jf.electronicstore.cart.domain.model.ProductId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
public class ProductController {
    public static final String PRODUCTS_MAPPING = "/products";
    public static final String COUPONS_MAPPING = PRODUCTS_MAPPING + "/{productId}/coupons";

    @Autowired
    private ProductApplicationService productApplicationService;

    @GetMapping(value = PRODUCTS_MAPPING)
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productApplicationService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    @PostMapping(value = PRODUCTS_MAPPING)
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productApplicationService.addProduct(productDTO);

        return ResponseEntity.created(generateLocation(createdProduct.getId())).body(createdProduct);
    }

    @DeleteMapping(value = PRODUCTS_MAPPING + "/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        productApplicationService.deleteProduct(ProductId.from(productId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = COUPONS_MAPPING)
    public ResponseEntity<List<CouponDTO>> getAllCoupons(@PathVariable Long productId) {
        List<CouponDTO> coupons = productApplicationService.getAllCoupons(ProductId.from(productId));

        return ResponseEntity.ok(coupons);
    }

    @PostMapping(value = COUPONS_MAPPING)
    public ResponseEntity<ProductDTO> addProductCoupon(@PathVariable Long productId,
                                                   @RequestBody CouponDTO couponDTO) {

        ProductDTO updatedProduct = productApplicationService.addCoupon(ProductId.from(productId), couponDTO);

        return ResponseEntity.created(generateLocation(updatedProduct.getId())).body(updatedProduct);
    }

    private static URI generateLocation(Long id) {
        return ServletUriComponentsBuilder
                .fromPath(PRODUCTS_MAPPING + "/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}


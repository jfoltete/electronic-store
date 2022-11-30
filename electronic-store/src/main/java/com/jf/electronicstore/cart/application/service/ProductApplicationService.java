package com.jf.electronicstore.cart.application.service;

import com.jf.electronicstore.cart.application.dto.CouponDTO;
import com.jf.electronicstore.cart.application.dto.ProductDTO;
import com.jf.electronicstore.cart.domain.factory.CouponFactory;
import com.jf.electronicstore.cart.domain.factory.ProductFactory;
import com.jf.electronicstore.cart.domain.model.Coupon;
import com.jf.electronicstore.cart.domain.model.Product;
import com.jf.electronicstore.cart.domain.model.ProductId;
import com.jf.electronicstore.cart.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductApplicationService {
    private final ProductRepository productRepository;
    private final ProductFactory productFactory;
    private final CouponFactory couponFactory;

    @Autowired
    public ProductApplicationService(ProductRepository productRepository, ProductFactory productFactory, CouponFactory couponFactory) {
        this.productRepository = productRepository;
        this.productFactory = productFactory;
        this.couponFactory = couponFactory;
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO.getName(), productDTO.getPrice(), productDTO.getAvailability());

        return ProductDTO.from(productRepository.save(product));
    }

    public void deleteProduct(ProductId productId) {
        productRepository.deleteById(productId.getId());
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDTO::from).toList();
    }

    public List<CouponDTO> getAllCoupons(ProductId productId) {
        Product product = productFactory.retrieve(productId);

        return product.getCoupons().stream().map(CouponDTO::from).toList();
    }

    public ProductDTO addCoupon(ProductId productId, CouponDTO couponDTO) {
        Product product = productFactory.retrieve(productId);
        Coupon coupon = couponFactory.create(product, couponDTO.targetQuantity(), couponDTO.discountPercent());

        product.add(coupon);
        productRepository.flush();

        return ProductDTO.from(product);
    }
}

package com.jf.electronicstore.cart.domain.repository;

import com.jf.electronicstore.cart.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
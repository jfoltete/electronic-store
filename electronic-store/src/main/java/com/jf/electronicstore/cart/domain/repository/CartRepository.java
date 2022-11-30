package com.jf.electronicstore.cart.domain.repository;

import com.jf.electronicstore.cart.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {}

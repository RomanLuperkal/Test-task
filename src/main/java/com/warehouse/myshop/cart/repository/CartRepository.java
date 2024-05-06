package com.warehouse.myshop.cart.repository;

import com.warehouse.myshop.cart.model.Cart;
import com.warehouse.myshop.cart.model.CartKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartKey> {

}

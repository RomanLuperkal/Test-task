package com.warehouse.myshop.curt.repository;

import com.warehouse.myshop.curt.model.Cart;
import com.warehouse.myshop.curt.model.CartKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartKey> {

}

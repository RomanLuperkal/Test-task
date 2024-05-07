package com.warehouse.myshop.cart.repository;

import com.warehouse.myshop.cart.model.Cart;
import com.warehouse.myshop.cart.model.CartKey;
import com.warehouse.myshop.product.dto.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartKey> {
    @Query("SELECT new com.warehouse.myshop.product.dto.ProductDto(c.product.uuid, c.product.name, c.quantity, c.price) " +
            "FROM Cart c " +
            "WHERE c.order.id = :order_id")
    List<ProductDto> findOrderProductsByOrderId(@Param("order_id") UUID order_id);
}

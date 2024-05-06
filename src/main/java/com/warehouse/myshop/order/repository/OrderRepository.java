package com.warehouse.myshop.order.repository;

import com.warehouse.myshop.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    /*@Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.carts c " +
            "LEFT JOIN FETCH c.product " +
            "LEFT JOIN FETCH o.customer " +
            "WHERE o.id = :order_id")*/
    @Query("select o from Order o " +
            "JOIN FETCH o.carts as c " +
            "JOIN FETCH o.customer " +
            "JOIN FETCH c.product " +
            "where o.id = :order_id")
    Optional<Order> findOrderByOrderId(@Param("order_id") UUID order_id);
}

package com.warehouse.myshop.orderedproduct.repository;

import com.warehouse.myshop.orderedproduct.model.OrderedProduct;
import com.warehouse.myshop.orderedproduct.model.OrderedProductKey;
import com.warehouse.myshop.product.dto.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct, OrderedProductKey> {
    @Query("SELECT new com.warehouse.myshop.product.dto.ProductDto(c.product.uuid, c.product.name, c.quantity, c.price) " +
            "FROM OrderedProduct c " +
            "WHERE c.order.id = :order_id")
    List<ProductDto> findOrderProductsByOrderId(@Param("order_id") UUID order_id);

    @Query("SELECT op FROM OrderedProduct op " +
            "JOIN FETCH op.order AS o " +
            "JOIN FETCH op.product AS p " +
            "JOIN FETCH o.customer " +
            "WHERE o.status = 'CREATED' " +
            "OR o.status = 'CONFIRMED'")
    Set<OrderedProduct> findFullOrderedProductsWithStatusCreatedOrConfirmed();
}

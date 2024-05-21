package com.warehouse.myshop.orderedproduct.model;


import com.warehouse.myshop.order.model.Order;
import com.warehouse.myshop.product.model.Product;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@IdClass(OrderedProductKey.class)
@Table(name = "ordered_product")
public class OrderedProduct {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_amount")
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderedProduct orderedProduct = (OrderedProduct) o;
        return order != null && Objects.equals(order, orderedProduct.order)
                && product != null && Objects.equals(product, orderedProduct.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}

package com.warehouse.myshop.cart.model;


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
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@IdClass(CartKey.class)
public class Cart {
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
        Cart cart = (Cart) o;
        return order != null && Objects.equals(order, cart.order)
                && product != null && Objects.equals(product, cart.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}

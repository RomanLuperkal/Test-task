package com.warehouse.myshop.orderedproduct.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProductKey implements Serializable {
    private UUID order;
    private UUID product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderedProductKey orderedProductKey = (OrderedProductKey) o;
        return order != null && Objects.equals(order, orderedProductKey.order)
                && product != null && Objects.equals(product, orderedProductKey.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}

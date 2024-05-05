package com.warehouse.myshop.curt.model;

import com.warehouse.myshop.order.model.Ordering;
import com.warehouse.myshop.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Embeddable
public class CartKey implements Serializable {
    //@Column(name = "order_id")
    private UUID order;
    //@Column(name = "product_id")
    private UUID product;
    /*@ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Ordering order;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;*/
}

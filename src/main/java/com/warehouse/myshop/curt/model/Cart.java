package com.warehouse.myshop.curt.model;


import com.warehouse.myshop.order.model.Ordering;
import com.warehouse.myshop.product.model.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@IdClass(CartKey.class)
public class Cart {
    /*@EmbeddedId
    private CartKey id;*/

   /* @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Ordering ordering;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "uuid", insertable = false, updatable = false)
    private Product product;*/
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Ordering order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_amount")
    private BigDecimal price;
}

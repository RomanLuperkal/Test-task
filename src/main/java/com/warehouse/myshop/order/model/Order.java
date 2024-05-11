package com.warehouse.myshop.order.model;

import com.warehouse.myshop.orderedproduct.model.OrderedProduct;
import com.warehouse.myshop.customer.model.Customer;
import com.warehouse.myshop.order.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "customer_order")
public class Order {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.CREATED;
    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;
    @OneToMany(mappedBy = "order")
    private Set<OrderedProduct> orderedProducts = new HashSet<>();
}

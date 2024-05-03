package com.warehouse.myshop.order.model;

import com.warehouse.myshop.customer.model.Customer;
import com.warehouse.myshop.order.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "id")
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;
}

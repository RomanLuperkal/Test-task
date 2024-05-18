package com.warehouse.myshop.order.dto;

import com.warehouse.myshop.customer.dto.CustomerInfo;
import com.warehouse.myshop.order.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderInfo {
    UUID id;

    CustomerInfo customer;

    Status status;

    String deliveryAddress;

    Integer quantity;
}

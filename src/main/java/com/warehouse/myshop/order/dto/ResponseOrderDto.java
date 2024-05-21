package com.warehouse.myshop.order.dto;

import com.warehouse.myshop.order.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ResponseOrderDto {
    private UUID id;
    private Long customerId;
    private Status status;
    private String deliveryAddress;
}

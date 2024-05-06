package com.warehouse.myshop.order.service;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.dto.UpdateOrderDto;

import java.util.UUID;

public interface OrderService {
    ResponseOrderDto createOrder(CreateOrderDto orderDto, Long customerId);
    ResponseOrderDto updateOrder(UpdateOrderDto updateOrder, Long customerId, UUID orderId);
}

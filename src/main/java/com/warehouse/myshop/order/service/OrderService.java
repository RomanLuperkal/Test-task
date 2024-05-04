package com.warehouse.myshop.order.service;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;

public interface OrderService {
    ResponseOrderDto createOrder(CreateOrderDto orderDto);
}

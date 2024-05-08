package com.warehouse.myshop.order.service;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseFullOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.product.dto.ShortProductDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    ResponseOrderDto createOrder(CreateOrderDto orderDto, Long customerId);
    ResponseOrderDto updateOrder(List<ShortProductDto> updateOrder, Long customerId, UUID orderId);
    ResponseFullOrderDto getOrder(UUID orderId, Long customerId);
    void deleteOrder(UUID orderId, Long customerId);
}

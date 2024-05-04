package com.warehouse.myshop.order.service;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.model.Order;
import com.warehouse.myshop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    @Override
    public ResponseOrderDto createOrder(CreateOrderDto orderDto) {
        List<Order> orders = new ArrayList<>();
        orderRepository.saveAll(orders);
        return null;
    }
}

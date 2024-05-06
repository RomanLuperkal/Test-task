package com.warehouse.myshop.order.controller;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.service.OrderService;
import com.warehouse.myshop.product.dto.ShortProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@Slf4j
@Validated
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseOrderDto> createOrder(@RequestHeader("customerId") @Min(1) Long customerId,
                                                        @RequestBody @Valid CreateOrderDto order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order, customerId));
    }

    @PatchMapping("{orderId}")
    public ResponseEntity<ResponseOrderDto> updateOrder(@RequestHeader("customerId") @Min(1) Long customerId,
                                                        @RequestBody @Valid List<ShortProductDto> updateOrder,
                                                        @PathVariable UUID orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(updateOrder, customerId, orderId));
    }
}

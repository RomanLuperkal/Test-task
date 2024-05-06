package com.warehouse.myshop.order.controller;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.dto.UpdateOrderDto;
import com.warehouse.myshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
                                                        @RequestBody @Valid UpdateOrderDto updateOrder, @PathVariable UUID orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(updateOrder, customerId, orderId));
    }
}

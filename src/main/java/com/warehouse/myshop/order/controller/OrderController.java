package com.warehouse.myshop.order.controller;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.OrderInfo;
import com.warehouse.myshop.order.dto.ResponseFullOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.dto.StatusDto;
import com.warehouse.myshop.order.service.OrderService;
import com.warehouse.myshop.product.dto.ShortProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
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

    @GetMapping("{orderId}")
    public ResponseEntity<ResponseFullOrderDto> getOrder(@PathVariable UUID orderId,
                                                         @RequestHeader("customerId") @Min(1) Long customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(orderId, customerId));
    }

    @DeleteMapping("{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId,
                                            @RequestHeader("customerId") @Min(1) Long customerId) {
        orderService.deleteOrder(orderId, customerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("{orderId}/confirm")
    public void confirmOrder(@PathVariable UUID orderId,
                             @RequestHeader("customerId") @Min(1) Long customerId) {
    }

    @PatchMapping ("{orderId}/status")
    public ResponseEntity<ResponseOrderDto> changeStatusOrder(@PathVariable UUID orderId,
                                                              @RequestBody @Valid StatusDto status) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.changeStatusOrder(orderId, status));
    }

    @GetMapping("info")
    public ResponseEntity<Map<UUID, List<OrderInfo>>> getOrdersInfo() {
        return ResponseEntity.ok(orderService.getOrdersInfo());
    }
}

package com.warehouse.myshop.order.eventhandler;

import com.warehouse.myshop.kafka.KafkaOrderEvent;
import com.warehouse.myshop.order.enums.OrderEvent;
import com.warehouse.myshop.order.event.CreateOrderEvent;
import com.warehouse.myshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateOrderEventHandler implements OrderEventHandler<CreateOrderEvent>{
    private final OrderService orderService;

    @Override
    public boolean canHandle(KafkaOrderEvent kafkaOrderEvent) {
        return OrderEvent.CREATE_ORDER.equals(kafkaOrderEvent.getEvent());
    }

    @Override
    public void handleEvent(CreateOrderEvent event) {
        orderService.createOrder(event.getCreateOrderDto(), event.getCustomerId());
    }
}

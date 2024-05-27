package com.warehouse.myshop.order.eventhandler;

import com.warehouse.myshop.kafka.KafkaOrderEvent;
import com.warehouse.myshop.order.enums.OrderEvent;
import com.warehouse.myshop.order.event.UpdateOrderEvent;
import com.warehouse.myshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateOrderEventHandler implements OrderEventHandler<UpdateOrderEvent>{
    private final OrderService orderService;

    @Override
    public boolean canHandle(KafkaOrderEvent kafkaOrderEvent) {
        return OrderEvent.UPDATE_ORDER.equals(kafkaOrderEvent.getEvent());
    }

    @Override
    public void handleEvent(UpdateOrderEvent event) {
        orderService.updateOrder(event.getUpdateOrderDto().getProducts(), event.getCustomerId(), event.getOrderId());
    }
}

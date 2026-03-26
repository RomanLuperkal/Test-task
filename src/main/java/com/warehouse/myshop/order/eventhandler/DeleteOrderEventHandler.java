package com.warehouse.myshop.order.eventhandler;

import com.warehouse.myshop.kafka.KafkaOrderEvent;
import com.warehouse.myshop.order.enums.OrderEvent;
import com.warehouse.myshop.order.event.DeleteOrderEvent;
import com.warehouse.myshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeleteOrderEventHandler implements OrderEventHandler<DeleteOrderEvent> {
    private final OrderService orderService;
    @Override
    public boolean canHandle(KafkaOrderEvent kafkaOrderEvent) {
        return OrderEvent.DELETE_ORDER.equals(kafkaOrderEvent.getEvent());
    }

    @Override
    public void handleEvent(DeleteOrderEvent event) {
        orderService.deleteOrder(event.getOrderId(), event.getCustomerId());
    }
}

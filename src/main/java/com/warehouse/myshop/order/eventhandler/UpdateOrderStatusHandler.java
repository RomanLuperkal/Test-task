package com.warehouse.myshop.order.eventhandler;

import com.warehouse.myshop.kafka.KafkaOrderEvent;
import com.warehouse.myshop.order.enums.OrderEvent;
import com.warehouse.myshop.order.event.UpdateOrderStatusEvent;
import com.warehouse.myshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UpdateOrderStatusHandler implements OrderEventHandler<UpdateOrderStatusEvent> {
    private final OrderService orderService;

    @Override
    public boolean canHandle(KafkaOrderEvent kafkaOrderEvent) {
        return OrderEvent.UPDATE_ORDER_STATUS.equals(kafkaOrderEvent.getEvent());
    }

    @Override
    public void handleEvent(UpdateOrderStatusEvent event) {
        orderService.changeStatusOrder(event.getOrderId(), event.getStatusDto());
    }
}

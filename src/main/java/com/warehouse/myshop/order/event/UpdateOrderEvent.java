package com.warehouse.myshop.order.event;

import com.warehouse.myshop.kafka.KafkaOrderEvent;
import com.warehouse.myshop.order.dto.UpdateOrderDto;
import com.warehouse.myshop.order.enums.OrderEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateOrderEvent implements KafkaOrderEvent {
    private UUID orderId;
    private Long customerId;
    private UpdateOrderDto updateOrderDto;

    @Override
    public OrderEvent getEvent() {
        return OrderEvent.UPDATE_ORDER;
    }
}

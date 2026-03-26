package com.warehouse.myshop.order.event;

import com.warehouse.myshop.kafka.KafkaOrderEvent;
import com.warehouse.myshop.order.dto.StatusDto;
import com.warehouse.myshop.order.enums.OrderEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateOrderStatusEvent implements KafkaOrderEvent {
    private UUID orderId;
    private StatusDto statusDto;

    @Override
    public OrderEvent getEvent() {
        return OrderEvent.UPDATE_ORDER_STATUS;
    }
}

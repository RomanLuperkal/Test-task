package com.warehouse.myshop.order.event;

import com.warehouse.myshop.kafka.KafkaOrderEvent;
import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.enums.OrderEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderEvent implements KafkaOrderEvent {
    private Long customerId;
    private CreateOrderDto createOrderDto;

    @Override
    public OrderEvent getEvent() {
        return OrderEvent.CREATE_ORDER;
    }
}

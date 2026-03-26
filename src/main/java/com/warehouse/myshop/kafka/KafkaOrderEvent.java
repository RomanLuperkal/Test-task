package com.warehouse.myshop.kafka;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.warehouse.myshop.order.enums.OrderEvent;
import com.warehouse.myshop.order.event.CreateOrderEvent;
import com.warehouse.myshop.order.event.DeleteOrderEvent;
import com.warehouse.myshop.order.event.UpdateOrderEvent;
import com.warehouse.myshop.order.event.UpdateOrderStatusEvent;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "event"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateOrderEvent.class, name = "CREATE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderEvent.class, name = "UPDATE_ORDER"),
        @JsonSubTypes.Type(value = DeleteOrderEvent.class, name = "DELETE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderStatusEvent.class, name = "UPDATE_ORDER_STATUS"),
})
public interface KafkaOrderEvent {
    OrderEvent getEvent();
}

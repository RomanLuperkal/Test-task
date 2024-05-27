package com.warehouse.myshop.order.eventhandler;

import com.warehouse.myshop.kafka.KafkaOrderEvent;

public interface OrderEventHandler<T extends KafkaOrderEvent> {
    boolean canHandle(KafkaOrderEvent kafkaOrderEvent);

    void handleEvent(T event);
}

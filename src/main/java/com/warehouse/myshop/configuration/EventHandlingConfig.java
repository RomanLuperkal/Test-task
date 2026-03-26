package com.warehouse.myshop.configuration;

import com.warehouse.myshop.kafka.KafkaOrderEvent;
import com.warehouse.myshop.order.eventhandler.OrderEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class EventHandlingConfig {

    @Bean
    <T extends KafkaOrderEvent> Set<OrderEventHandler<T>> eventHandlers(Set<OrderEventHandler<T>> eventHandlers) {
        return new HashSet<>(eventHandlers);
    }
}

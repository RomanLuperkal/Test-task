package com.warehouse.myshop.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.order.eventhandler.OrderEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled")
public class Consumer {
    private final Set<OrderEventHandler<KafkaOrderEvent>> eventHandlers;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "test_topic", containerFactory = "kafkaListenerContainerFactoryByte")
    public void listenGroupTopic2(byte[] message) {
        log.info("Получено сообщение: {}", message);

        try {
            KafkaOrderEvent kafkaOrderEvent = objectMapper.readValue(message, KafkaOrderEvent.class);

            System.out.println(kafkaOrderEvent);

            eventHandlers.stream().filter(eventHandler -> eventHandler.canHandle(kafkaOrderEvent)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Handler for eventsource not found"))
                    .handleEvent(kafkaOrderEvent);
        } catch (Exception e) {
            log.error("Couldn't parse message: {}; exception: ", message, e);
        }


        /*try {
            final KafkaEvent eventSource = objectMapper.readValue(message, KafkaEvent.class);
            log.info("EventSource: {}", eventSource);

            eventHandlers.stream()
                    .filter(eventSourceEventHandler -> eventSourceEventHandler.canHandle(eventSource))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Handler for eventsource not found"))
                    .handleEvent(eventSource);

        } catch (JsonProcessingException e) {
            log.error("Couldn't parse message: {}; exception: ", message, e);
        }*/
    }
}

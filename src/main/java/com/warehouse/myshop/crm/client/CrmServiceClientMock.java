package com.warehouse.myshop.crm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@ConditionalOnProperty(name = "rest.crm-service.mock.enabled")
@Primary
public class CrmServiceClientMock implements CrmServiceClient {
    @Override
    public CompletableFuture<Map<String, String>> getAccountNumbers(Set<String> logins) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> result = null;
            try {
                log.info("Выдаю инн");
                Random random = new Random();
                long lowerBound = 100000000000L;
                long upperBound = 999999999999L;
                result = logins.stream().collect(Collectors
                        .toMap(Function.identity(), l -> String.valueOf(lowerBound + (Math.abs(random.nextLong()) % (upperBound - lowerBound)))));
                log.info("Засыпаю на 3 сек");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return result;
        });
    }
}

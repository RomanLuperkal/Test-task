package com.warehouse.myshop.account.client;

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
@ConditionalOnProperty(name = "rest.account-service.mock.enabled")
@Primary
public class AccountServiceClientMock implements AccountServiceClient {
    @Override
    public CompletableFuture<Map<String, String>> getAccountNumbers(Set<String> logins) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> result = null;
            try {
                log.info("Выдаю номера аккаунтов");
                Random random = new Random();
                result = logins.stream().collect(Collectors
                        .toMap(Function.identity(), l -> String.valueOf(10000000 + random.nextInt(90000000))));
                log.info("Засыпаю на 3 сек");
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return result;
        });
    }
}

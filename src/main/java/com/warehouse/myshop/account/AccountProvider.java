package com.warehouse.myshop.account;

import com.warehouse.myshop.account.client.AccountServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AccountProvider {
    private final AccountServiceClient accountServiceClient;

    public CompletableFuture<Map<String, String>> getAccountNumbers(Set<String> logins) {
        return accountServiceClient.getAccountNumbers(logins);
    }
}

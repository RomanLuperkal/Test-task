package com.warehouse.myshop.account.client;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface AccountServiceClient {
    CompletableFuture<Map<String, String>> getAccountNumbers(Set<String> logins);
}

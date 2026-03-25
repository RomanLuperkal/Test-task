package com.warehouse.myshop.crm.client;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface CrmServiceClient {
    CompletableFuture<Map<String, String>> getAccountNumbers(Set<String> logins);
}

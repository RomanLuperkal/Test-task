package com.warehouse.myshop.crm;

import com.warehouse.myshop.crm.client.CrmServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CrmProvider {
    private final CrmServiceClient crmServiceClient;

    public CompletableFuture<Map<String, String>> getInns(Set<String> logins) {
        return crmServiceClient.getAccountNumbers(logins);
    }
}

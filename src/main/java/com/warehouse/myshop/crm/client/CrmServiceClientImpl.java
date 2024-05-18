package com.warehouse.myshop.crm.client;

import com.warehouse.myshop.configuration.CrmServiceProperties;
import com.warehouse.myshop.handler.exceptions.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CrmServiceClientImpl implements CrmServiceClient {
    @Qualifier("CrmServiceWebClient")
    private final WebClient webClient;
    private final CrmServiceProperties crmServiceProperties;

    @Override
    public CompletableFuture<Map<String, String>> getAccountNumbers(Set<String> logins) {
        return this.webClient.post()
                .uri(crmServiceProperties.getMethods().get("post-inn"))
                .body(Mono.just(logins), new ParameterizedTypeReference<>() {})
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new ResponseStatusException("Ошибка отправки get запроса в currency service со статусом: "
                                + clientResponse.statusCode())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new ResponseStatusException("Ошибка отправки get запроса в currency service со статусом: "
                                + clientResponse.statusCode())))
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .toFuture();
    }
}

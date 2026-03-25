package com.warehouse.myshop.currency.client;

import com.warehouse.myshop.configuration.CurrencyServiceProperties;
import com.warehouse.myshop.currency.dto.ResponseCurrencyDto;
import com.warehouse.myshop.handler.exceptions.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

    @Qualifier("currencyServiceWebClient")
    private final WebClient webClient;
    private final CurrencyServiceProperties currencyServiceProperties;

    private ResponseCurrencyDto performGetRequest() {
        return this.webClient.get()
                .uri(currencyServiceProperties.getMethods().get("get-currency"))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ResponseStatusException("Ошибка отправки get запроса в currency service со статусом: "
                                + clientResponse.statusCode())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ResponseStatusException("Ошибка отправки get запроса в currency service со статусом: "
                                + clientResponse.statusCode())))
                .bodyToMono(ResponseCurrencyDto.class)
                .retry(2)
                .block();
    }

    @Override
    @Cacheable(value = "currencies", unless = "#result == null")
    public ResponseCurrencyDto getCurrenciesRate() {
        log.info("Получение курса валют");
        return performGetRequest();
    }
}

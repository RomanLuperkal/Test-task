package com.warehouse.myshop.currency.client;

import com.warehouse.myshop.currency.dto.ResponseCurrencyDto;
import com.warehouse.myshop.handler.exceptions.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@ConditionalOnExpression("!${currency-service.mock}")
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

    private final WebClient webClient;
    @Value("${currency-service.methods.get-currency}")
    private String getCurrencyUri;

    public CurrencyServiceClientImpl(@Value("${currency-service.host}") String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }


    private <T> T performGetRequest(String uri, Class<T> clazz) {
        return this.webClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new ResponseStatusException("Ошибка отправки get запроса в currency service со статусом: "
                                + clientResponse.statusCode())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new ResponseStatusException("Ошибка отправки get запроса в currency service со статусом: "
                                + clientResponse.statusCode())))
                .bodyToMono(clazz)
                .retry(2)
                .block();
    }

    @Override
    @Cacheable(value = "currencies", unless = "#result == null")
    public ResponseCurrencyDto getCurrenciesRate() {
        log.info("Получение курса валют");
        return performGetRequest(getCurrencyUri, ResponseCurrencyDto.class);
    }
}

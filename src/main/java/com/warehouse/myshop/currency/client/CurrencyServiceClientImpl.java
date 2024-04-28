package com.warehouse.myshop.currency.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.currency.dto.ResponseCurrencyDto;
import com.warehouse.myshop.handler.exceptions.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class CurrencyServiceClientImpl implements CurrencyServiceClient {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private final WebClient webClient;
    @Value("${currency-service.methods.get-currency}")
    private String getCurrencyUri;

    public CurrencyServiceClientImpl(@Value("${currency-service.host}") String url, @Autowired ResourceLoader resourceLoader,
                                     @Autowired ObjectMapper objectMapper) {
        this.webClient = WebClient.builder().baseUrl(url).build();
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
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
    public ResponseCurrencyDto getCurrency() {
        try {
            return performGetRequest(getCurrencyUri, ResponseCurrencyDto.class);
        } catch (Exception e) {
            try {
                return objectMapper.readValue(readJsonResource("exchange-rate.json"), ResponseCurrencyDto.class);
            } catch (Exception e2) {
                throw new RuntimeException(e);
            }
        }

    }

    private String readJsonResource(String resourcePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + resourcePath);
            Path path = resource.getFile().toPath();
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать json файл");
        }
    }
}

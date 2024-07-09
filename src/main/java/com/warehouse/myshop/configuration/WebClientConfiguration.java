package com.warehouse.myshop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {
    private final CurrencyServiceProperties currencyServiceProperties;
    private final AccountServiceProperties accountServiceProperties;
    private final CrmServiceProperties crmServiceProperties;

    @Bean()
    public WebClient currencyServiceWebClient() {
        return WebClient.builder().baseUrl(currencyServiceProperties.getHost()).build();
    }

    @Bean()
    public WebClient accountServiceWebClient() {
        return WebClient.builder().baseUrl(accountServiceProperties.getHost()).build();
    }

    @Bean()
    public WebClient crmServiceWebClient() {
        return WebClient.builder().baseUrl(crmServiceProperties.getHost()).build();
    }
}

package com.warehouse.myshop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {
    private final CurrencyServiceProperties currencyServiceProperties;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl(currencyServiceProperties.getHost()).build();
    }
}

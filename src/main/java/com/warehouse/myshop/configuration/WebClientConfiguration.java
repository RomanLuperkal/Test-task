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

    @Bean(name = "CurrencyServiceWebClient")
    public WebClient getWebClientForCurrencyService() {
        return WebClient.builder().baseUrl(currencyServiceProperties.getHost()).build();
    }

    @Bean(name = "AccountServiceWebClient")
    public WebClient getWebClientForAccountService() {
        return WebClient.builder().baseUrl(accountServiceProperties.getHost()).build();
    }

    @Bean(name = "CrmServiceWebClient")
    public WebClient getWebClientForCrmService() {
        return WebClient.builder().baseUrl(crmServiceProperties.getHost()).build();
    }
}

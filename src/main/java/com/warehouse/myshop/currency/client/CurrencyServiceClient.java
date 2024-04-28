package com.warehouse.myshop.currency.client;

import com.warehouse.myshop.currency.dto.ResponseCurrencyDto;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyServiceClient {
    ResponseCurrencyDto getCurrency();
}

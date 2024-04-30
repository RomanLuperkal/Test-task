package com.warehouse.myshop.currency.client;

import com.warehouse.myshop.currency.dto.ResponseCurrencyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
@Slf4j
@ConditionalOnExpression("${currency-service.mock}")
public class CurrencyServiceClientMock implements CurrencyServiceClient {
    @Override
    public ResponseCurrencyDto getCurrenciesRate() {
        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1;
        if (randomNumber < 6) {
            log.info("Выдаю курс валют");
            return ResponseCurrencyDto.builder().USD(new BigDecimal(2))
                    .EUR(new BigDecimal(10)).CNY(new BigDecimal(5)).build();
        }
        log.info("Выдаю ошибку");
        throw new RuntimeException();
    }
}

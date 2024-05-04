package com.warehouse.myshop.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.currency.client.CurrencyServiceClient;
import com.warehouse.myshop.currency.dto.ResponseCurrencyDto;
import com.warehouse.myshop.enums.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.util.annotation.Nullable;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateProvider {
    private final CurrencyServiceClient currencyServiceClient;
    private final ObjectMapper objectMapper;

    public BigDecimal getExchangeRate(Currency currency) {
        return Optional.ofNullable(getExchangeRateFromService(currency))
                .orElseGet(() -> getExchangeRateFromFile(currency));
    }

    private @Nullable BigDecimal getExchangeRateFromService(Currency currency) {
        try {
            return Optional.ofNullable(currencyServiceClient.getCurrenciesRate()).map(rate -> getExchangeRateByCurrency(rate, currency))
                    .orElse(null);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

    private BigDecimal getExchangeRateFromFile(Currency currency) {
        try {
            log.info("Чтение курса валют из файла");
            ResponseCurrencyDto responseCurrencyDto = objectMapper.
                    readValue(readJsonResource("exchange-rate.json"), ResponseCurrencyDto.class);
            return Optional.ofNullable(responseCurrencyDto).map(rate -> getExchangeRateByCurrency(rate, currency))
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BigDecimal getExchangeRateByCurrency(ResponseCurrencyDto rate, Currency currency) {
        switch (currency) {
            case USD:
                return rate.getUSD();
            case EUR:
                return rate.getEUR();
            case CNY:
                return rate.getCNY();
            case RUB:
                return BigDecimal.ONE;
            default:
                return null;
        }
    }

    private String readJsonResource(String resourcePath) {
        try {
            Path path = Path.of("target/classes/" + resourcePath);
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать json файл");
        }
    }
}

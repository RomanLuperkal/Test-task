package com.warehouse.myshop.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ResponseCurrencyDto {
    @JsonProperty("USD")
    private BigDecimal USD;
    @JsonProperty("EUR")
    private BigDecimal EUR;
    @JsonProperty("CNY")
    private BigDecimal CNY;

    public BigDecimal getCurrencyFromString(String currency) {
        switch (currency) {
            case "USD":
                return USD;
            case "EUR":
                return EUR;
            case "CNY":
                return CNY;
            default:
                return null;
        }
    }
}

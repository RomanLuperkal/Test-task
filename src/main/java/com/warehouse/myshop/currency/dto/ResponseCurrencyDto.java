package com.warehouse.myshop.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.myshop.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCurrencyDto {
    @JsonProperty("USD")
    private BigDecimal USD;
    @JsonProperty("EUR")
    private BigDecimal EUR;
    @JsonProperty("CNY")
    private BigDecimal CNY;

    public BigDecimal getCurrencyFromString(Currency currency) {
        switch (currency) {
            case USD:
                return this.USD;
            case EUR:
                return this.EUR;
            case CNY:
                return this.CNY;
            default:
                return null;
        }
    }
}

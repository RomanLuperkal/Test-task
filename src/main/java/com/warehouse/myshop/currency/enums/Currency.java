package com.warehouse.myshop.currency.enums;

public enum Currency {
    RUB, EUR, CNY, USD;

    public static Currency getCurrency(String currency) {
        switch (currency) {
            case "RUB":
                return RUB;
            case "EUR":
                return EUR;
            case "CNY":
                return CNY;
            case "USD":
                return USD;
            default:
                return null;
        }
    }
}

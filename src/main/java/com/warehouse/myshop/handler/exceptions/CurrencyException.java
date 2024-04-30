package com.warehouse.myshop.handler.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrencyException extends RuntimeException {
    private String message;
}

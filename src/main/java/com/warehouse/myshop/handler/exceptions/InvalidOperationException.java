package com.warehouse.myshop.handler.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidOperationException extends RuntimeException {
    private final String message;
}

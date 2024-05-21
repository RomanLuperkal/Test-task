package com.warehouse.myshop.handler.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderException extends RuntimeException {
    private String message;
}

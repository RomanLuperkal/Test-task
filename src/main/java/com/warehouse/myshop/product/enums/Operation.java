package com.warehouse.myshop.product.enums;

import com.warehouse.myshop.handler.exceptions.InvalidOperationException;

public enum Operation {
    EQUALS("="),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN_OR_EQUALS("<="),
    LIKE("~");

    private final String code;

    Operation(String code) {
        this.code = code;
    }

    public static Operation fromString(String text) {
        if (text == null) {
            throw new InvalidOperationException("Operation не может быть null");
        }
        for (Operation operationType : Operation.values()) {
            if (operationType.name().equals(text) || operationType.code.equals(text)) {
                return operationType;
            }
        }
        throw new InvalidOperationException("Не найдено константы с текстом: " + text);
    }
}

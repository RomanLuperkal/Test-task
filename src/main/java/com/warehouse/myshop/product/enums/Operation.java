package com.warehouse.myshop.product.enums;

import java.util.HashMap;
import java.util.Map;

public enum Operation {
    EQUALS("=", new String[]{"EQUALS", "="}),
    GREATER_THAN_OR_EQUALS(">=", new String[]{"GREATER_THAN_OR_EQUALS", ">="}),
    LESS_THAN_OR_EQUALS("<=", new String[]{"LESS_THAN_OR_EQUALS", "<="}),
    LIKE("~", new String[]{"LIKE", "~"});

    private final String symbol;
    private final String[] aliases;
    private static final Map<String, Operation> ALIASES_MAP = new HashMap<>();

    static {
        for (Operation op : Operation.values()) {
            for (String alias : op.aliases) {
                ALIASES_MAP.put(alias, op);
            }
        }
    }

    Operation(String symbol, String[] aliases) {
        this.symbol = symbol;
        this.aliases = aliases;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Operation fromString(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        Operation operation = ALIASES_MAP.get(text.toUpperCase());
        if (operation == null) {
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
        return operation;
    }
}

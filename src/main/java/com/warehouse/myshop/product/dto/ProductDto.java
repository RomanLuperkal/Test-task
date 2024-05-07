package com.warehouse.myshop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductDto {
    private UUID uuid;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}

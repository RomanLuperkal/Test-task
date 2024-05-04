package com.warehouse.myshop.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ShortProductDto {
    private UUID id;
    private Long quantity;
}

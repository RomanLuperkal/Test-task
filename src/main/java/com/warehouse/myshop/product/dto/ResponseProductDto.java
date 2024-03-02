package com.warehouse.myshop.product.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class ResponseProductDto {
    private UUID uuid;
    private String name;
    private String articleNumber;
    private String description;
    private Long categoryId;
    private Double price;
    private Integer quantity;
    private LocalDateTime lastUpdate;
    private LocalDateTime creationDate;
}

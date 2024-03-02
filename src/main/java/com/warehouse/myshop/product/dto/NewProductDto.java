package com.warehouse.myshop.product.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Builder
@Data
public class NewProductDto {
    @NotBlank
    private String name;
    @NotBlank
    private String articleNumber;
    @NotBlank
    private String description;
    @Positive
    private Long categoryId;
    @Positive
    private Double price;
    @Positive
    private Integer quantity;
}

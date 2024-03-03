package com.warehouse.myshop.product.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder
@Data
public class UpdateProductDto {
    @Size(min = 4, max = 2000, message = "Некорректное имя")
    private String name;
    @Positive
    private String articleNumber;
    @Size(min = 4, max = 7000, message = "Некорректное описание")
    private String description;
    @Positive(message = "Некоректный индификатор категории")
    private Long categoryId;
    @Positive(message = "Недопустимая цена")
    private Double price;
    @Min(value = 0, message = "Недопустимое количество товара")
    private Integer quantity;
}

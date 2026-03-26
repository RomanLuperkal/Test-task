package com.warehouse.myshop.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewProductDto {
    @Size(min = 4, max = 2000, message = "Некорректное имя")
    private String name;
    @NotBlank(message = "Недопустимый артикул")
    private String articleNumber;
    @Size(min = 4, max = 7000, message = "Некорректное описание")
    private String description;
    @Positive(message = "Некоректный индификатор категории")
    private Long categoryId;
    @Positive(message = "Недопустимая стоимость товара")
    private BigDecimal price;
    @PositiveOrZero(message = "Количество товара не может быть отрицательным")
    private Integer quantity;
}

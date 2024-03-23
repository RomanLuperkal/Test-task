package com.warehouse.myshop.product.dto;

import lombok.*;

import javax.validation.constraints.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDto {
    @Size(min = 4, max = 2000, message = "Некорректное имя")
    private String name;
    @NotBlank(message = "Недопустимый артикул")
    private String articleNumber;
    @Size(min = 4, max = 7000, message = "Некорректное описание")
    private String description;
    @Positive(message = "Некоректный индификатор категории")
    private Long categoryId;
    @Positive(message = "Недопустимая стоимость товара")
    private Double price;
    @PositiveOrZero(message = "Количество товара не может быть отрицательным")
    private Integer quantity;
}

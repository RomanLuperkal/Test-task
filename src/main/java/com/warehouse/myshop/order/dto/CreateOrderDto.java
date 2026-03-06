package com.warehouse.myshop.order.dto;

import com.warehouse.myshop.product.dto.ShortProductDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CreateOrderDto {
    @NotBlank(message = "Недопустимый deliveryAddress")
    private String deliveryAddress;
    @NotNull(message = "products не может быть пустым")
    @NotEmpty(message = "products не может быть пустым")
    private List<ShortProductDto> products;
}

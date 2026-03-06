package com.warehouse.myshop.order.dto;

import com.warehouse.myshop.product.dto.ShortProductDto;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UpdateOrderDto {
    @NotNull(message = "products не может быть пустым")
    @NotEmpty(message = "products не может быть пустым")
    //@JsonValue
    private List<ShortProductDto> products;
}

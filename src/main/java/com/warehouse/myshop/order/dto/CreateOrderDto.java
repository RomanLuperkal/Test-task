package com.warehouse.myshop.order.dto;

import com.warehouse.myshop.product.dto.ShortProductDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

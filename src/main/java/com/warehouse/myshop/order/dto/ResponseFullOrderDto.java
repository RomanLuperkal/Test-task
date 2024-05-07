package com.warehouse.myshop.order.dto;

import com.warehouse.myshop.product.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ResponseFullOrderDto {
    private UUID orderId;
    private List<ProductDto> products;
    private BigDecimal totalPrice;
}

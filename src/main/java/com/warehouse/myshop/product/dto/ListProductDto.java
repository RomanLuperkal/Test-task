package com.warehouse.myshop.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ListProductDto {
    List<ResponseProductDto> products;
}

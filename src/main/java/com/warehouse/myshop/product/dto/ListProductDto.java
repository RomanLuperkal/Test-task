package com.warehouse.myshop.product.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ListProductDto {
    @JsonValue
    List<ResponseProductDto> products;
}

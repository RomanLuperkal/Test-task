package com.warehouse.myshop.category.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ListCategoryDto {
    @JsonValue
    private List<CategoryDtoResp> categories;
}

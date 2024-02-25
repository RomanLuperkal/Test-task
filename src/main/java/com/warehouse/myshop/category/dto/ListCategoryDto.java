package com.warehouse.myshop.category.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ListCategoryDto {
    private List<CategoryDtoResp> categories;
}

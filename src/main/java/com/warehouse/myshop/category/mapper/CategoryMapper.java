package com.warehouse.myshop.category.mapper;

import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDtoResp;
import com.warehouse.myshop.category.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category mapToCategory(NewCategoryDto category);

    @Mapping(source = "categoryId", target = "id")
    NewCategoryDtoResp mapToNewCategoryDtoResp(Category category);
}

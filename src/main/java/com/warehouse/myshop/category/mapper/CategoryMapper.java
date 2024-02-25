package com.warehouse.myshop.category.mapper;

import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category mapToCategory(NewCategoryDto category);

    @Mapping(source = "categoryId", target = "id")
    CategoryDtoResp mapToCategoryDtoResp(Category category);

    List<CategoryDtoResp> mapToListCategoryDto(Page<Category> page);
}

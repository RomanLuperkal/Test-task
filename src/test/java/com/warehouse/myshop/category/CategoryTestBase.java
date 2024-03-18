package com.warehouse.myshop.category;

import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.model.Category;

public abstract class CategoryTestBase {

    protected Category createCategory() {
        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("testCategory");
        return category;
    }

    protected NewCategoryDto createNewCategoryDto(Category category) {
        return NewCategoryDto.builder()
                .name(category.getName())
                .build();
    }

    protected UpdateCategoryDto createUpdatedCategoryDto(Category category) {
        return UpdateCategoryDto.builder()
                .name(category.getName())
                .build();
    }

    protected CategoryDtoResp createCategoryDtoResp(Category category) {
        return CategoryDtoResp.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .build();
    }
}

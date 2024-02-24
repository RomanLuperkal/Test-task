package com.warehouse.myshop.category.service;

import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDtoResp;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;

public interface CategoryService {
    NewCategoryDtoResp createCategory(NewCategoryDto category);

    NewCategoryDtoResp updateCategory(UpdateCategoryDto category, Long id);
}

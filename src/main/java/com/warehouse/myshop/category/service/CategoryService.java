package com.warehouse.myshop.category.service;

import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.dto.ListCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDtoResp createCategory(NewCategoryDto category);

    CategoryDtoResp updateCategory(UpdateCategoryDto category, Long id);

    void deleteCategory(Long id);

    CategoryDtoResp getCategoryById(Long id);

    ListCategoryDto getCategories(Pageable pageable);
}

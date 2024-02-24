package com.warehouse.myshop.category.service;

import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDtoResp;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.mapper.CategoryMapper;
import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public NewCategoryDtoResp createCategory(NewCategoryDto category) {
        Category newCategory = categoryRepository.save(mapper.mapToCategory(category));
        return mapper.mapToNewCategoryDtoResp(newCategory);
    }

    @Override
    public NewCategoryDtoResp updateCategory(UpdateCategoryDto updateCategory, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id=" + id + " was not found"));
        category.setName(updateCategory.getName());
        return mapper.mapToNewCategoryDtoResp(category);
    }
}

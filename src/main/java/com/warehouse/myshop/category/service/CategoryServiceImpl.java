package com.warehouse.myshop.category.service;

import com.warehouse.myshop.category.dto.ListCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.mapper.CategoryMapper;
import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDtoResp createCategory(NewCategoryDto category) {
        Category newCategory = categoryRepository.save(mapper.mapToCategory(category));
        return mapper.mapToCategoryDtoResp(newCategory);
    }

    @Override
    public CategoryDtoResp updateCategory(UpdateCategoryDto updateCategory, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Категории с id=" + id + " не найдено"));
        category.setName(updateCategory.getName());
        return mapper.mapToCategoryDtoResp(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id))
            throw new NotFoundException("Категории с id=" + id + " не найдено");
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDtoResp getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Категории с id=" + id + " не найдено"));
        return mapper.mapToCategoryDtoResp(category);
    }

    @Override
    public ListCategoryDto getCategories(Pageable pageable) {
        return ListCategoryDto.builder()
                .categories(mapper.mapToListCategoryDto(categoryRepository.findAll(pageable)))
                .build();
    }

    @Override
    public Boolean existsCategory(Long id) {
        return categoryRepository.existsById(id);
    }
}

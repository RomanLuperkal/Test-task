package com.warehouse.myshop.category.service;

import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.mapper.CategoryMapperImpl;
import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    private final CategoryMapperImpl mapper = new CategoryMapperImpl();

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository, mapper);
    }

    private Category category;

    @Test
    void createCategoryTest() {
       category = new Category();
       initCategory(category);
       when(categoryRepository.save(any(Category.class))).thenReturn(category);
       NewCategoryDto newCategoryDto = NewCategoryDto.builder().name(category.getName()).build();
       CategoryDtoResp expectedCategory  = CategoryDtoResp.builder().id(1L).name(category.getName()).build();

       CategoryDtoResp actualCategory = categoryService.createCategory(newCategoryDto);

       assertEquals(expectedCategory, actualCategory);
       verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategoryTest() {
        category = new Category();
        initCategory(category);
        Category updatedCategory = new Category();
        updatedCategory.setName("testUpdatedCategory");
        //when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        UpdateCategoryDto updateCategoryDto = UpdateCategoryDto.builder()
                .name(updatedCategory.getName())
                .build();
        CategoryDtoResp expectedCategory  = CategoryDtoResp.builder()
                .id(category.getCategoryId())
                .name(updatedCategory.getName())
                .build();

        CategoryDtoResp actualCategory = categoryService.updateCategory(updateCategoryDto, 1L);

        assertEquals(expectedCategory, actualCategory);
        //verify(categoryRepository, times(0)).save(any(Category.class));
    }

    private void initCategory(Category category) {
        category.setCategoryId(1L);
        category.setName("testCategory");
    }
}

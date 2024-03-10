package com.warehouse.myshop.category.service;

import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.mapper.CategoryMapperImpl;
import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void updateCategoryTestWhenCategoryWasNotFound() {
        category = new Category();
        initCategory(category);
        Category updatedCategory = new Category();
        updatedCategory.setName("testUpdatedCategory");
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        UpdateCategoryDto updateCategoryDto = UpdateCategoryDto.builder()
                .name(updatedCategory.getName())
                .build();

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> categoryService.updateCategory(updateCategoryDto, 1L));
        assertThat(e.getMessage()).contains("Категории с id=1 не найдено");
    }

    @Test
    void deleteCategoryTest() {
        doNothing().when(categoryRepository).deleteById(1L);
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategoryWhenCategoryWasNotFound() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1L));
        assertThat(e.getMessage()).contains("Категории с id=1 не найдено");

        verify(categoryRepository, times(0)).deleteById(1L);
    }

    @Test
    void getCategoryByIdTest() {
        category = new Category();
        initCategory(category);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        CategoryDtoResp expectedCategory  = CategoryDtoResp.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .build();

        CategoryDtoResp actualCategory = categoryService.getCategoryById(1L);

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void getCategoryByIdTestWhenCategoryWasNotFound() {
        category = new Category();
        initCategory(category);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(1L));
        assertThat(e.getMessage()).contains("Категории с id=1 не найдено");
    }

    @Test
    void getCategoriesTest() {
        /*ListCategoryDto expectedCategories = ListCategoryDto.builder()
                .categories(List.of(new CategoryDtoResp(1L, "testCategory")))
                .build();*/
    }

    private void initCategory(Category category) {
        category.setCategoryId(1L);
        category.setName("testCategory");
    }
}

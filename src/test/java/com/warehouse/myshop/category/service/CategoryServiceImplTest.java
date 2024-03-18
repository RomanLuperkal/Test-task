package com.warehouse.myshop.category.service;

import com.warehouse.myshop.category.CategoryTestBase;
import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.dto.ListCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.mapper.CategoryMapperImpl;
import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryServiceImplTest extends CategoryTestBase {
    @Autowired
    private CategoryServiceImpl categoryService;
    @MockBean
    private CategoryRepository categoryRepository;

    private final CategoryMapperImpl mapper = new CategoryMapperImpl();

    private Category category;

    @Test
    void createCategoryTest() {
       category = createCategory();
       when(categoryRepository.save(any(Category.class))).thenReturn(category);
       NewCategoryDto newCategoryDto = createNewCategoryDto(category);
       CategoryDtoResp expectedCategory  = createCategoryDtoResp(category);

       CategoryDtoResp actualCategory = categoryService.createCategory(newCategoryDto);

       assertEquals(expectedCategory, actualCategory);
       verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategoryTest() {
        category = createCategory();
        Category updatedCategory = new Category();
        updatedCategory.setName("testUpdatedCategory");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        UpdateCategoryDto updateCategoryDto = createUpdatedCategoryDto(updatedCategory);
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
        Category updatedCategory = new Category();
        updatedCategory.setName("testUpdatedCategory");
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        UpdateCategoryDto updateCategoryDto = createUpdatedCategoryDto(category);

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
        category = createCategory();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        CategoryDtoResp expectedCategory  = createCategoryDtoResp(category);

        CategoryDtoResp actualCategory = categoryService.getCategoryById(1L);

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void getCategoryByIdTestWhenCategoryWasNotFound() {
        category = createCategory();
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(1L));
        assertThat(e.getMessage()).contains("Категории с id=1 не найдено");
    }

    @Test
    void getCategoriesTest() {
        List<Category> categories = List.of(new Category(1L, "testCategory"),
                new Category(2L, "testCategory"));
        Pageable pageable = PageRequest.of(0, 2);
        Page<Category> page = new PageImpl<>(categories,pageable,categories.size());
        ListCategoryDto expectedCategories =
                ListCategoryDto.builder()
                .categories(mapper.mapToListCategoryDto(page))
                .build();

        when(categoryRepository.findAll(pageable)).thenReturn(page);

        ListCategoryDto actualCategories = categoryService.getCategories(pageable);

        assertEquals(expectedCategories.getCategories(), actualCategories.getCategories());
    }
}

package com.warehouse.myshop.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.category.CategoryTestBase;
import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.dto.ListCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ActiveProfiles("local")
class CategoryControllerTest {
    private final MockMvc mockMvc;
    private  final ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;
    private static NewCategoryDto newCategoryDto;
    private static CategoryDtoResp categoryDtoResp;
    private static UpdateCategoryDto updateCategoryDto;
    private static ListCategoryDto listCategoryDto;

    private final String url = "/categories";

    @BeforeAll
    public static void createDto() {
        newCategoryDto = NewCategoryDto.builder()
                .name("testCategory")
                .build();
        categoryDtoResp = CategoryDtoResp.builder()
                .id(1L)
                .name(newCategoryDto.getName())
                .build();
        updateCategoryDto = UpdateCategoryDto.builder()
                .name("updateCategory")
                .build();
        listCategoryDto = ListCategoryDto.builder()
                .categories(List.of(categoryDtoResp))
                .build();
    }

    @Test
    @SneakyThrows
    void createCategoryTest() {
        when(categoryService.createCategory(any(NewCategoryDto.class))).thenReturn(categoryDtoResp);
        mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsString(newCategoryDto))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpectAll(
                        status().isCreated(),
                        content().json(objectMapper.writeValueAsString(categoryDtoResp))
                );
    }

    @Test
    @SneakyThrows
    void createCategoryDuplicate() {
        when(categoryService.createCategory(any(NewCategoryDto.class))).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(newCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    void createInvalidNameCategory() {
        NewCategoryDto invalidCategory = NewCategoryDto.builder()
                .name("  test")
                .build();
        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(invalidCategory))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    @SneakyThrows
    void updateCategoryTest() {
        when(categoryService.updateCategory(any(UpdateCategoryDto.class), eq(1L))).thenReturn(categoryDtoResp);
        mockMvc.perform(patch(url + "/1")
                .content(objectMapper.writeValueAsString(updateCategoryDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(categoryDtoResp))
                );
    }

    @Test
    @SneakyThrows
    void updateInvalidNameCategory() {
        UpdateCategoryDto invalidCategory = UpdateCategoryDto.builder()
                .name("  test")
                .build();
        when(categoryService.updateCategory(any(UpdateCategoryDto.class), eq(1L))).thenReturn(categoryDtoResp);
        mockMvc.perform(patch(url + "/1")
                        .content(objectMapper.writeValueAsString(invalidCategory))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(
                        status().isBadRequest()
                ).andDo(print());
    }

    @Test
    @SneakyThrows
    void updateCategoryWhenInvalidPatchVariable() {
        mockMvc.perform(patch(url + "/0")
                        .content(objectMapper.writeValueAsString(updateCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest()
                );
        verify(categoryService, times(0)).updateCategory(any(UpdateCategoryDto.class), anyLong());
    }

    @Test
    @SneakyThrows
    void deleteCategory() {
        doNothing().when(categoryService).deleteCategory(anyLong());

        mockMvc.perform(delete(url + "/1"))

                .andExpect(status().isNoContent());
        verify(categoryService, times(1)).deleteCategory(anyLong());
    }

    @Test
    @SneakyThrows
    void deleteCategoryWhenInvalidPatchVariable() {
        mockMvc.perform(delete(url + "/0"))

                .andExpect(status().isBadRequest());
        verify(categoryService, times(0)).deleteCategory(anyLong());
    }

    @Test
    @SneakyThrows
    void getCategoryTest() {
        when(categoryService.getCategoryById(eq(1L))).thenReturn(categoryDtoResp);
        mockMvc.perform(get(url + "/1"))

                .andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(categoryDtoResp))
        );
    }

    @Test
    @SneakyThrows
    void getCategoryWhenInvalidPatchVariable() {
        mockMvc.perform(get(url + "/0"))

                .andExpect(
                        status().isBadRequest()
                );
        verify(categoryService, times(0)).getCategoryById(anyLong());
    }

    @Test
    @SneakyThrows
    void getCategories() {
        when(categoryService.getCategories(any(Pageable.class))).thenReturn(listCategoryDto);
        mockMvc.perform(get(url))

                .andExpectAll(
                status().isOk(),
                content().json(objectMapper.writeValueAsString(listCategoryDto)));
    }

    @Test
    @SneakyThrows
    void getCategoriesWhenInvalidRequestParam() {
        mockMvc.perform(get(url).param("from", "-1"))

                .andExpect(
                        status().isBadRequest()
                );
    }
}

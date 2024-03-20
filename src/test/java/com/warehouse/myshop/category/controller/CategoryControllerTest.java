package com.warehouse.myshop.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.category.CategoryTestBase;
import com.warehouse.myshop.category.dto.CategoryDtoResp;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class CategoryControllerTest extends CategoryTestBase {
    private final MockMvc mockMvc;
    private  final ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;
    private static NewCategoryDto newCategoryDto;
    private static CategoryDtoResp categoryDtoResp;
    private static UpdateCategoryDto updateCategoryDto;

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
        when(categoryService.createCategory(any(NewCategoryDto.class))).thenReturn(categoryDtoResp);
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
        when(categoryService.updateCategory(any(UpdateCategoryDto.class), anyLong())).thenReturn(categoryDtoResp);
        mockMvc.perform(patch(url + "/0")
                        .content(objectMapper.writeValueAsString(updateCategoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest()
                );
    }
}

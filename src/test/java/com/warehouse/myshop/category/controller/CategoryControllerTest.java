package com.warehouse.myshop.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.category.service.CategoryService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CategoryService categoryService;
}

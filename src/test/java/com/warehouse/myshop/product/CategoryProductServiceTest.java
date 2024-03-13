package com.warehouse.myshop.product;

import com.warehouse.myshop.category.service.CategoryService;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.service.CategoryProductService;
import com.warehouse.myshop.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryProductServiceTest {
    @Autowired
    private CategoryProductService categoryProductService;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @Test
    void createProductTest() {
        when(categoryService.existsCategory(1L)).thenReturn(true);

        NewProductDto productDto = NewProductDto.builder()
                .categoryId(1L)
                .name("test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(10)
                .build();
        ResponseProductDto expectedProduct = ResponseProductDto.builder()
                .uuid(UUID.randomUUID())
                .name(productDto.getName())
                .categoryId(productDto.getCategoryId())
                .articleNumber(productDto.getArticleNumber())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .quantity(productDto.getQuantity())
                .creationDate(LocalDateTime.now())
                .build();
        when(productService.createProduct(productDto)).thenReturn(expectedProduct);

        ResponseProductDto actualProduct = categoryProductService.createProduct(productDto);

        assertEquals(expectedProduct, actualProduct);
        verify(categoryService, times(1)).existsCategory(1L);
        verify(productService, times(1)).createProduct(productDto);
    }
}

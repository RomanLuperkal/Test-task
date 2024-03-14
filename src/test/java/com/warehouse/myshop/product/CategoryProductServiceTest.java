package com.warehouse.myshop.product;

import com.warehouse.myshop.category.service.CategoryService;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.service.CategoryProductService;
import com.warehouse.myshop.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void createProductTestWhenCategoryWasNotFound() {
        when(categoryService.existsCategory(1L)).thenReturn(false);
        NewProductDto productDto = NewProductDto.builder()
                .categoryId(1L)
                .name("test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(10)
                .build();

        String expectedMessage = "Категории с id=" + productDto.getCategoryId() + " не найдено";

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> categoryProductService.createProduct(productDto));

        assertEquals(expectedMessage, e.getMessage());
        verify(categoryService, times(1)).existsCategory(1L);
        verify(productService, times(0)).createProduct(productDto);
    }

    @Test
    void updateProductTest() {
        UpdateProductDto product = UpdateProductDto.builder()
                .categoryId(1L)
                .name("test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(10)
                .build();
        when(categoryService.existsCategory(product.getCategoryId())).thenReturn(true);
        ResponseProductDto expectedProduct = ResponseProductDto.builder()
                .uuid(UUID.randomUUID())
                .name(product.getName())
                .categoryId(product.getCategoryId())
                .articleNumber(product.getArticleNumber())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .creationDate(LocalDateTime.now().minusDays(1))
                .lastUpdate(LocalDateTime.now())
                .build();
        when(productService.updateProduct(expectedProduct.getUuid(), product)).thenReturn(expectedProduct);

        ResponseProductDto actualProduct = categoryProductService.updateProduct(expectedProduct.getUuid(), product);

        assertEquals(expectedProduct, actualProduct);

        verify(categoryService, times(1)).existsCategory(anyLong());
        verify(productService, times(1)).updateProduct(expectedProduct.getUuid(), product);
    }
}

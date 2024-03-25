package com.warehouse.myshop.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.product.ProductTestBase;
import com.warehouse.myshop.product.audit.ProductAudit;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.service.ProductService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductControllerTest extends ProductTestBase {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    private static NewProductDto newProductDto;
    private static ResponseProductDto responseProductDto;
    private static UpdateProductDto updateProductDto;
    private static ListProductDto listProductDto;
    private final String url = "/products";

    @BeforeAll
    public static void createDto() {
        newProductDto = NewProductDto.builder()
                .categoryId(1L)
                .name("test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(10)
                .build();

        category.setId(newProductDto.getCategoryId());
        ProductAudit productAudit = new ProductAudit();
        productAudit.setCreationDate(LocalDateTime.now());
        responseProductDto = ResponseProductDto.builder()
                .uuid(UUID.randomUUID())
                .name(newProductDto.getName())
                .category(category)
                .articleNumber(newProductDto.getArticleNumber())
                .description(newProductDto.getDescription())
                .price(newProductDto.getPrice())
                .quantity(newProductDto.getQuantity())
                .productAudit(productAudit)
                .build();
        updateProductDto = UpdateProductDto.builder()
                .categoryId(1L)
                .name("test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(5)
                .build();
        listProductDto = ListProductDto.builder()
                .products(List.of(responseProductDto))
                .build();
    }

    @Test
    @SneakyThrows
    void createProduct() {
        when(productService.createProduct(any(NewProductDto.class))).thenReturn(responseProductDto);
        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(newProductDto))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpectAll(
                        status().isCreated(),
                        content().json(objectMapper.writeValueAsString(responseProductDto))
                );
    }

    @Test
    @SneakyThrows
    void createProductDuplicate() {
        when(productService.createProduct(any(NewProductDto.class))).thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(newProductDto))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    void createInvalidProduct() {
        NewProductDto invalidProduct = NewProductDto.builder()
                .categoryId(1L)
                .name("  test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(-10)
                .build();
        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(invalidProduct))
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    @SneakyThrows
    void updateProductTest() {
        when(productService.updateProduct(any(UUID.class), any(UpdateProductDto.class))).thenReturn(responseProductDto);
        mockMvc.perform(patch(url+ "/" + UUID.randomUUID())
                .content(objectMapper.writeValueAsString(updateProductDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                    status().isOk(),
                    content().json(objectMapper.writeValueAsString(responseProductDto))
        );
    }

    @Test
    @SneakyThrows
    void invalidUpdateProduct() {
        NewProductDto invalidProduct = NewProductDto.builder()
                .categoryId(1L)
                .name("  test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(-10)
                .build();
        mockMvc.perform(patch(url+ "/" + UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(invalidProduct))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    @SneakyThrows
    void updateProductWhenInvalidUUID() {
        mockMvc.perform(patch(url+ "/qwqfqw" )
                        .content(objectMapper.writeValueAsString(updateProductDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    @SneakyThrows
    void deleteProduct() {
        doNothing().when(productService).deleteProduct(any(UUID.class));
        mockMvc.perform(delete(url + "/" + UUID.randomUUID()))

                .andExpect(status().isNoContent());
        verify(productService, times(1)).deleteProduct(any(UUID.class));
    }

    @Test
    @SneakyThrows
    void deleteProductWhenInvalidUUID() {
        mockMvc.perform(delete(url+ "/qwqfqw" ))
                .andExpectAll(
                        status().isBadRequest()
                );
        verify(productService, times(0)).deleteProduct(any(UUID.class));
    }

    @Test
    @SneakyThrows
    void getProductTest() {
        when(productService.getProduct(any(UUID.class))).thenReturn(responseProductDto);
        mockMvc.perform(get(url + "/" + UUID.randomUUID()))

                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(responseProductDto))
                );
    }

    @Test
    @SneakyThrows
    void getProductWhenInvalidUUID() {
        mockMvc.perform(get(url + "/sffaf"))

                .andExpect(
                        status().isBadRequest()
                );
        verify(productService, times(0)).deleteProduct(any(UUID.class));
    }

    @Test
    @SneakyThrows
    void getProducts() {
        when(productService.getProducts(any(Pageable.class))).thenReturn(listProductDto);
        mockMvc.perform(get(url))

                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(listProductDto)));
    }

    @Test
    @SneakyThrows
    void getProductWhenInvalidRequestParam() {
        mockMvc.perform(get(url).param("from", "-1"))

                .andExpect(
                        status().isBadRequest()
                );
    }
}

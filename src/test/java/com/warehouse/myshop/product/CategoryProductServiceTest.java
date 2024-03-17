package com.warehouse.myshop.product;

import com.warehouse.myshop.category.service.CategoryService;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.mapper.ProductMapper;
import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.service.CategoryProductService;
import com.warehouse.myshop.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryProductServiceTest extends ProductTestBase {
    @Autowired
    private CategoryProductService categoryProductService;
    @MockBean
    private ProductService productService;
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private ProductMapper mapper;

    @Test
    void createProductTest() {
        when(categoryService.existsCategory(1L)).thenReturn(true);
        NewProductDto productDto = createNewProductDto();
        ResponseProductDto expectedProduct = createExpectedResponseDto(productDto);
        when(productService.createProduct(productDto)).thenReturn(expectedProduct);

        ResponseProductDto actualProduct = categoryProductService.createProduct(productDto);

        assertEquals(expectedProduct, actualProduct);
        verify(categoryService, times(1)).existsCategory(1L);
        verify(productService, times(1)).createProduct(productDto);
    }

    @Test
    void createProductTestWhenCategoryWasNotFound() {
        when(categoryService.existsCategory(1L)).thenReturn(false);
        NewProductDto productDto = createNewProductDto();

        String expectedMessage = "Категории с id=" + productDto.getCategoryId() + " не найдено";

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> categoryProductService.createProduct(productDto));

        assertEquals(expectedMessage, e.getMessage());
        verify(categoryService, times(1)).existsCategory(1L);
        verify(productService, times(0)).createProduct(productDto);
    }

    @Test
    void updateProductTest() {
        UpdateProductDto product = createUpdateProductDto();
        when(categoryService.existsCategory(product.getCategoryId())).thenReturn(true);
        ResponseProductDto expectedProduct = createExpectedResponseDto(product);
        when(productService.updateProduct(expectedProduct.getUuid(), product)).thenReturn(expectedProduct);

        ResponseProductDto actualProduct = categoryProductService.updateProduct(expectedProduct.getUuid(), product);

        assertEquals(expectedProduct, actualProduct);

        verify(categoryService, times(1)).existsCategory(anyLong());
        verify(productService, times(1)).updateProduct(expectedProduct.getUuid(), product);
    }

    @Test
    void updateProductTestWhenCategoryNotFound() {
        UpdateProductDto product = createUpdateProductDto();
        String expectedMessage = "Категории с id=" + product.getCategoryId() + " не найдено";
        when(categoryService.existsCategory(product.getCategoryId())).thenReturn(false);
        ResponseProductDto expectedProduct = createExpectedResponseDto(product);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> categoryProductService.updateProduct(expectedProduct.getUuid(), product));

        assertThat(expectedMessage).contains(e.getMessage());
        verify(categoryService, times(1)).existsCategory(product.getCategoryId());
        verify(productService, times(0)).updateProduct(expectedProduct.getUuid(), product);
    }

    @Test
    void deleteProductTest() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(productService).deleteProduct(uuid);

        categoryProductService.deleteProduct(uuid);

        verify(productService, times(1)).deleteProduct(uuid);
    }

    @Test
    void getProductTest() {
        ResponseProductDto expectedProduct = createExpectedResponseDto(createNewProductDto());
        when(productService.getProduct(expectedProduct.getUuid())).thenReturn(expectedProduct);

        ResponseProductDto actualProduct = categoryProductService.getProduct(expectedProduct.getUuid());

        assertEquals(expectedProduct, actualProduct);
        verify(productService, times(1)).getProduct(expectedProduct.getUuid());
    }

    @Test
    void getProductsTest() {
        Product product = createProduct(createNewProductDto(), UUID.randomUUID());
        List<Product> products = List.of(product);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Product> page = new PageImpl<>(products,pageable,products.size());
        ListProductDto expectedProducts = ListProductDto.builder().
                products(mapper.mapToListResponseProductDto(page)).build();
        when(productService.getProducts(pageable)).thenReturn(expectedProducts);

        ListProductDto actualProducts = categoryProductService.getProducts(pageable);

        assertEquals(expectedProducts, actualProducts);
        verify(productService, times(1)).getProducts(pageable);
    }
}

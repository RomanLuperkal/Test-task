package com.warehouse.myshop.product.service;

import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.product.ProductTestBase;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.mapper.ProductMapper;
import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.repository.ProductRepository;
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
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceImplTest extends ProductTestBase {
    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @Autowired
    ProductMapper mapper;

    @Test
    void createProductTest() {
        NewProductDto product = createNewProductDto();
        UUID uuid = UUID.randomUUID();
        Product expectedProduct = createProduct(product, uuid);
        expectedProduct.setUuid(uuid);
        ResponseProductDto expectedProductDto = createExpectedResponseDto(product);
        expectedProductDto.setUuid(uuid);
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        ResponseProductDto actualProduct = productService.createProduct(product);

        assertEquals(expectedProductDto, actualProduct);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProductTest() {
        UpdateProductDto updateProduct = createUpdateProductDto();
        ResponseProductDto expectedProductDto = createExpectedResponseDto(updateProduct);
        Product product = createProduct(createNewProductDto(), expectedProductDto.getUuid());
        product.setPrice(10d);
        when(productRepository.findById(expectedProductDto.getUuid())).thenReturn(Optional.of(product));

        ResponseProductDto actualProduct = productService.updateProduct(expectedProductDto.getUuid(), updateProduct);

        assertEquals(expectedProductDto.getPrice(), actualProduct.getPrice());
        verify(productRepository, times(1)).findById(expectedProductDto.getUuid());
    }

    @Test
    void updateProductTestWhenProductWasNotFound() {
        UpdateProductDto updateProduct = createUpdateProductDto();
        ResponseProductDto expectedProductDto = createExpectedResponseDto(updateProduct);
        String expectedMessage = "Товара с UUID=" + expectedProductDto.getUuid() + " не существует";
        when(productRepository.findById(expectedProductDto.getUuid())).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> productService.updateProduct(expectedProductDto.getUuid(), updateProduct));

        assertThat(e.getMessage()).contains(expectedMessage);
        verify(productRepository, times(1)).findById(expectedProductDto.getUuid());
    }

    @Test
    void deleteProductTest() {
        UUID uuid = UUID.randomUUID();
        doNothing().when(productRepository).deleteById(uuid);
        when(productRepository.existsById(uuid)).thenReturn(true);

        productService.deleteProduct(uuid);

        verify(productRepository, times(1)).deleteById(uuid);
        verify(productRepository, times(1)).existsById(uuid);
    }

    @Test
    void deleteProductTestWhenProductNotFound() {
        UUID uuid = UUID.randomUUID();
        String expectedMessage = "Товара с UUID=" + uuid + " не существует";
        when(productRepository.existsById(uuid)).thenReturn(false);

        NotFoundException e = assertThrows(NotFoundException.class, () -> productService.deleteProduct(uuid));

        assertThat(e.getMessage()).contains(expectedMessage);
        verify(productRepository, times(1)).existsById(uuid);
    }

    @Test
    void getProductTest() {
        ResponseProductDto expectedProduct = createExpectedResponseDto(createNewProductDto());
        Product product = createProduct(createNewProductDto(), expectedProduct.getUuid());
        when(productRepository.findById(expectedProduct.getUuid())).thenReturn(Optional.of(product));

        ResponseProductDto actualProduct = productService.getProduct(expectedProduct.getUuid());

        assertEquals(expectedProduct, actualProduct);
        verify(productRepository, times(1)).findById(expectedProduct.getUuid());
    }

    @Test
    void getProductTestWhenProductWasNotFound() {
        ResponseProductDto expectedProduct = createExpectedResponseDto(createNewProductDto());
        String expectedMessage = "Товара с UUID=" + expectedProduct.getUuid() + " не существует";
        when(productRepository.findById(expectedProduct.getUuid())).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> productService.getProduct(expectedProduct.getUuid()));

        assertThat(e.getMessage()).contains(expectedMessage);
        verify(productRepository, times(1)).findById(expectedProduct.getUuid());
    }

    @Test
    void getProductsTest() {
        Product product = createProduct(createNewProductDto(), UUID.randomUUID());
        List<Product> products = List.of(product);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Product> page = new PageImpl<>(products,pageable,products.size());
        ListProductDto expectedProducts = ListProductDto.builder().
                products(mapper.mapToListResponseProductDto(page)).build();
        when(productRepository.findAll(pageable)).thenReturn(page);

        ListProductDto actualProducts = productService.getProducts(pageable);

        assertEquals(expectedProducts.getProducts(), actualProducts.getProducts());
        verify(productRepository, times(1)).findAll(pageable);
    }
}

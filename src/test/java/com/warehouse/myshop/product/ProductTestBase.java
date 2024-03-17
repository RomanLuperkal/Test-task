package com.warehouse.myshop.product;

import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.mapper.ProductMapper;
import com.warehouse.myshop.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class ProductTestBase {
    @Autowired
    private ProductMapper mapper;

    protected ResponseProductDto createExpectedResponseDto(NewProductDto product) {
        return ResponseProductDto.builder()
                .uuid(UUID.randomUUID())
                .name(product.getName())
                .categoryId(product.getCategoryId())
                .articleNumber(product.getArticleNumber())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .creationDate(LocalDateTime.now())
                .build();
    }

    protected ResponseProductDto createExpectedResponseDto(UpdateProductDto product) {
        return ResponseProductDto.builder()
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
    }

    protected UpdateProductDto createUpdateProductDto() {
        return UpdateProductDto.builder()
                .categoryId(1L)
                .name("test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(10)
                .build();
    }

    protected NewProductDto createNewProductDto() {
        return NewProductDto.builder()
                .categoryId(1L)
                .name("test")
                .articleNumber("te-st-article")
                .description("test description")
                .price(50.5)
                .quantity(10)
                .build();
    }

    protected Product createProduct(NewProductDto productDto, UUID uuid) {
        Product product = mapper.mapToProduct(productDto);
        product.setUuid(uuid);
        return product;
    }
}

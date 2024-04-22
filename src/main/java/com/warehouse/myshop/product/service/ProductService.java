package com.warehouse.myshop.product.service;

import com.warehouse.myshop.product.dto.*;
import com.warehouse.myshop.product.model.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ResponseProductDto createProduct(NewProductDto productDto);

    ResponseProductDto updateProduct(UUID uuid, UpdateProductDto productDto);

    void deleteProduct(UUID uuid);

    ResponseProductDto getProduct(UUID uuid);

    ListProductDto getProducts(Pageable pageable);

    List<Product> searchProducts(List<FilterConditionDto<?>> conditions);
}

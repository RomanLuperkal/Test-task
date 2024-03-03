package com.warehouse.myshop.product.service;

import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    ResponseProductDto createProduct(NewProductDto productDto);

    ResponseProductDto updateProduct(UUID uuid, UpdateProductDto productDto);

    void deleteProduct(UUID uuid);

    ResponseProductDto getProduct(UUID uuid);

    ListProductDto getProducts(Pageable pageable);
}

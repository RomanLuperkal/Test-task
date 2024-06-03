package com.warehouse.myshop.product.service;


import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.dto.condition.FilterConditionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ResponseProductDto createProduct(NewProductDto productDto);

    ResponseProductDto updateProduct(UUID uuid, UpdateProductDto productDto);

    void deleteProduct(UUID uuid);

    ResponseProductDto getProduct(UUID uuid);

    ListProductDto getProducts(Pageable pageable);

    ListProductDto searchProducts(List<FilterConditionDto<?>> conditions, Pageable pageable);

    void uploadImage(UUID productId, MultipartFile file);
}

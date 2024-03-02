package com.warehouse.myshop.product.service;

import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;

public interface ProductService {
    ResponseProductDto createProduct(NewProductDto productDto);
}

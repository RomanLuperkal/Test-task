package com.warehouse.myshop.product.service;

import com.warehouse.myshop.category.service.CategoryService;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "CreateProductService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryProductService implements ProductService {
    private final CategoryService categoryService;
    private final ProductService origin;

    @Override
    public ResponseProductDto createProduct(NewProductDto productDto) {
        if (!categoryService.existsCategory(productDto.getCategoryId()))
            throw new NotFoundException("Такой категории не существует");
        return origin.createProduct(productDto);
    }
}

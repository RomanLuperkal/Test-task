package com.warehouse.myshop.product.service;

import com.warehouse.myshop.category.service.CategoryService;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service(value = "ProductServiceDecorator")
public class CategoryProductService implements ProductService {
    private final CategoryService categoryService;

    private final ProductService origin;

    public CategoryProductService(CategoryService categoryService, @Qualifier("ProductService") ProductService origin) {
        this.categoryService = categoryService;
        this.origin = origin;
    }

    @Override
    public ResponseProductDto createProduct(NewProductDto productDto) {
        if (!categoryService.existsCategory(productDto.getCategoryId()))
            throw new NotFoundException("Категории с id=" + productDto.getCategoryId() + " не найдено");
        return origin.createProduct(productDto);
    }

    @Override
    public ResponseProductDto updateProduct(UUID uuid, UpdateProductDto productDto) {
        if (!categoryService.existsCategory(productDto.getCategoryId()))
            throw new NotFoundException("Категории с id=" + productDto.getCategoryId() + " не найдено");
        return origin.updateProduct(uuid, productDto);
    }

    @Override
    public void deleteProduct(UUID uuid) {
        origin.deleteProduct(uuid);
    }

    @Override
    public ResponseProductDto getProduct(UUID uuid) {
        return origin.getProduct(uuid);
    }

    @Override
    public ListProductDto getProducts(Pageable pageable) {
        return origin.getProducts(pageable);
    }
}

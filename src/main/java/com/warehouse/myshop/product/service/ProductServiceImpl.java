package com.warehouse.myshop.product.service;

import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.category.service.CategoryService;
import com.warehouse.myshop.handler.exceptions.NotFoundException;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.mapper.ProductMapper;
import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ResponseProductDto createProduct(NewProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Категории с id=" + productDto.getCategoryId() + " не найдено"));
        Product product = mapper.mapToProduct(productDto);
        product.setCategory(category);
        return mapper.mapToResponseProductDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ResponseProductDto updateProduct(UUID uuid, UpdateProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Категории с id=" + productDto.getCategoryId() + " не найдено"));
        Product product = productRepository.findById(uuid).orElseThrow(
                () -> new NotFoundException("Товара с UUID=" + uuid + " не существует"));
        product.setCategory(category);
        return mapper.mapToResponseProductDto(mapper.mapToProduct(product, productDto));
    }

    @Override
    @Transactional
    public void deleteProduct(UUID uuid) {
        if (!productRepository.existsById(uuid))
            throw new NotFoundException("Товара с UUID=" + uuid + " не существует");
        productRepository.deleteById(uuid);
    }

    @Override
    public ResponseProductDto getProduct(UUID uuid) {
        Product product = productRepository.findById(uuid).orElseThrow(
                () -> new NotFoundException("Товара с UUID=" + uuid + " не существует"));
        return mapper.mapToResponseProductDto(product);
    }

    @Override
    public ListProductDto getProducts(Pageable pageable) {
        return ListProductDto.builder()
                .products(mapper.mapToListResponseProductDto(productRepository.findAll(pageable)))
                .build();
    }
}

package com.warehouse.myshop.product.service;

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
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Primary
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public ResponseProductDto createProduct(NewProductDto productDto) {
        Product product = productRepository.save(mapper.mapToProduct(productDto));
        return mapper.mapToResponseProductDto(product);
    }

    @Override
    public ResponseProductDto updateProduct(UUID uuid, UpdateProductDto productDto) {
        Product product = productRepository.findById(uuid).orElseThrow(
                () -> new NotFoundException("Товара с UUID=" + uuid + " не существует"));
        return mapper.mapToResponseProductDto(mapper.mapToProduct(product, productDto));
    }

    @Override
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

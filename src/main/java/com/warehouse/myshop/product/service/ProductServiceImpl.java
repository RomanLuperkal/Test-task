package com.warehouse.myshop.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.category.repository.CategoryRepository;
import com.warehouse.myshop.currency.client.CurrencyServiceClient;
import com.warehouse.myshop.currency.dto.ResponseCurrencyDto;
import com.warehouse.myshop.currency.session.CurrencyProvider;
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyProvider currencyProvider;
    private final ProductMapper mapper;
    private final CurrencyServiceClient currencyClient;
    private final ObjectMapper objectMapper;

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
        product.getProductAudit().setLastUpdate(LocalDateTime.now());
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
        ResponseProductDto responseProductDto = mapper.mapToResponseProductDto(product);
        String currency = currencyProvider.getCurrency();
        responseProductDto.setCurrency(currency);
        if (!currency.equals("RUB")) {
            ResponseCurrencyDto currenciesRate = getCurrenciesRate();
            responseProductDto.setPrice(responseProductDto.getPrice().multiply(currenciesRate.getCurrencyFromString(currency)));
        }
        return responseProductDto;
    }

    @Override
    public ListProductDto getProducts(Pageable pageable) {
        return ListProductDto.builder()
                .products(mapper.mapToListResponseProductDto(productRepository.findAll(pageable)))
                .build();
    }

    private String readJsonResource(String resourcePath) {
        try {
            Path path = Path.of("target/classes/" + resourcePath);
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать json файл");
        }
    }

    private ResponseCurrencyDto getCurrenciesRate() {
        try {
            return currencyClient.getCurrenciesRate();
        } catch (Exception e) {
            try {
                System.out.println("Читаю данные из файла");
                return objectMapper.readValue(readJsonResource("exchange-rate.json"), ResponseCurrencyDto.class);
            } catch (Exception e2) {
                throw new RuntimeException(e);
            }
        }
    }
}

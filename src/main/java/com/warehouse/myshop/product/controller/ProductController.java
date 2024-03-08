package com.warehouse.myshop.product.controller;

import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Slf4j
@Validated
public class ProductController {
    @Qualifier("ProductServiceDecorator")
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ResponseProductDto> createProduct(@Valid @RequestBody NewProductDto productDto) {
        log.info("Создание товара:{}", productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                productService.createProduct(productDto));
    }
    @PatchMapping("{uuid}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable UUID uuid,
                                                            @Valid @RequestBody UpdateProductDto productDto) {
        log.info("Обновление товара:{} с uuid:{}", productDto, uuid);
        return ResponseEntity.ok(productService.updateProduct(uuid, productDto));
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID uuid) {
        log.info("Удаление товара с uuid={}", uuid);
        productService.deleteProduct(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("{uuid}")
    public ResponseEntity<ResponseProductDto> getProduct(@PathVariable UUID uuid) {
        log.info("Получение товара с uuid={}", uuid);
        return ResponseEntity.ok(productService.getProduct(uuid));
    }

    @GetMapping
    public ResponseEntity<ListProductDto> getProducts(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.info("Получение страницы с товарами с form={} и size={}", from, size);
        return ResponseEntity.ok(productService.getProducts(PageRequest.of(from / size, size)));
    }
}

package com.warehouse.myshop.product.controller;

import com.warehouse.myshop.order.dto.OrderInfo;
import com.warehouse.myshop.product.dto.condition.FilterConditionDto;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Slf4j
@Validated
@RequiredArgsConstructor
@Tag(name = "Product controller", description = "Позволяет взаимодействовать с товарами")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Создание товара", description = "Позволяет создать товара")
    @PostMapping
    public ResponseEntity<ResponseProductDto> createProduct(@Valid @RequestBody NewProductDto productDto) {
        log.info("Создание товара:{}", productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                productService.createProduct(productDto));
    }

    @Operation(summary = "Обновление товара", description = "Позволяет обновить товар по его uuid")
    @PatchMapping("{uuid}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable UUID uuid,
                                                            @Valid @RequestBody UpdateProductDto productDto) {
        log.info("Обновление товара:{} с uuid:{}", productDto, uuid);
        return ResponseEntity.ok(productService.updateProduct(uuid, productDto));
    }

    @Operation(summary = "Удаление товара",
            description = "Позволяет удалить товар по его uuid")
    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID uuid) {
        log.info("Удаление товара с uuid={}", uuid);
        productService.deleteProduct(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Получение товара", description = "Позволяет получить товар по его uuid")
    @GetMapping("{uuid}")
    public ResponseEntity<ResponseProductDto> getProduct(@PathVariable UUID uuid) {
        log.info("Получение товара с uuid={}", uuid);
        return ResponseEntity.ok(productService.getProduct(uuid));
    }

    @Operation(summary = "Получение списка всех товаров",
            description = "Позволяет получить список всех товаров с заданными параметрами пагинации")
    @GetMapping
    public ResponseEntity<ListProductDto> getProducts(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.info("Получение страницы с товарами с form={} и size={}", from, size);
        return ResponseEntity.ok(productService.getProducts(PageRequest.of(from / size, size)));
    }

    @PostMapping("search")
    public ResponseEntity<ListProductDto> searchProducts(@RequestBody @Valid List<FilterConditionDto<?>> conditions,
                                                         Pageable pageable) {
        log.info("Получение товаров по условиям");
        return ResponseEntity.ok(productService.searchProducts(conditions, pageable));
    }

    @GetMapping("info")
    public ResponseEntity<Map<UUID, List<OrderInfo>>> getOrdersInfo() {
        log.info("Получение информации о товарах");
        return ResponseEntity.ok(productService.getProductsInfo());
    }
}

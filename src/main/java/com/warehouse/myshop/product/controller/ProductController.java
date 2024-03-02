package com.warehouse.myshop.product.controller;

import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/products")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {
    private final Map<String, ProductService> productServices;

    @PostMapping
    public ResponseEntity<ResponseProductDto> createProduct(@Valid @RequestBody NewProductDto productDto) {
        log.info("Создание продукта:{}", productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                productServices.get("CreateProductService").createProduct(productDto));
    }
}

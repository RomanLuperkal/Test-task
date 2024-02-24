package com.warehouse.myshop.category.controller;

import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDtoResp;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<NewCategoryDtoResp> createCategory(@RequestBody @Valid NewCategoryDto category) {
        log.info("create category:{}", category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));
    }

    @PatchMapping("{id}")
    public ResponseEntity<NewCategoryDtoResp> updateCategory(@RequestBody @Valid UpdateCategoryDto category,
                                                             @PathVariable @Min(1) Long id) {
        log.info("update category:{} with id={}",category, id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(category, id));
    }
}

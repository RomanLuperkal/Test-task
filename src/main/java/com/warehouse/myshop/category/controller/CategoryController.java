package com.warehouse.myshop.category.controller;

import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.category.dto.ListCategoryDto;
import com.warehouse.myshop.category.dto.NewCategoryDto;
import com.warehouse.myshop.category.dto.UpdateCategoryDto;
import com.warehouse.myshop.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/categories")
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Category controller", description = "Позволяет взаимодействовать с категориями товаров")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Создание категории товара", description = "Позволяет создать категорию товара")
    @PostMapping
    public ResponseEntity<CategoryDtoResp> createCategory(@RequestBody @Valid NewCategoryDto category) {
        log.info("create category:{}", category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));
    }

    @Operation(summary = "Обновление категории товара",
            description = "Позволяет обновить категорию товара по его индификатору")
    @PatchMapping("{id}")
    public ResponseEntity<CategoryDtoResp> updateCategory(@RequestBody @Valid UpdateCategoryDto category,
                                                          @PathVariable @Min(1) Long id) {
        log.info("update category:{} with id={}", category, id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(category, id));
    }

    @Operation(summary = "Удаление категории товара",
            description = "Позволяет удалить категорию товара по его индификатору")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Min(1) Long id) {
        log.info("delete category with id={}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Получение категории товара", description = "Позволяет получить категорию товара по его id")
    @GetMapping("{id}")
    public ResponseEntity<CategoryDtoResp> getCategoryById(@PathVariable @Min(1) Long id) {
        log.info("get category with id={}", id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Получение списка всех категорий",
            description = "Позволяет получить список всех категорий с заданными параметрами пагинации")
    @GetMapping
    public ResponseEntity<ListCategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                         @RequestParam(defaultValue = "10") @Min(10) Integer size) {
        log.info("get categories: from: {},size: {}", from, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.getCategories(PageRequest.of(from / size, size, Sort.by("categoryId").ascending())));
    }
}

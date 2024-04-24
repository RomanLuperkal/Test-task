package com.warehouse.myshop.product.service;

import com.warehouse.myshop.category.mapper.CategoryMapper;
import com.warehouse.myshop.category.mapper.CategoryMapperImpl;
import com.warehouse.myshop.product.ProductTestBase;
import com.warehouse.myshop.product.dto.FilterConditionDto;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.mapper.ProductMapperImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@ActiveProfiles("local")
@DataJpaTest
@Import({ProductServiceImpl.class, ProductMapperImpl.class, CategoryMapperImpl.class})
@Sql(scripts = {"/insert test category.sql", "/insert test product.sql"})
public class ProductServiceImplJpaTest extends ProductTestBase {
    @Autowired
    private ProductService productService;
    private List<FilterConditionDto<?>> conditions;

    @BeforeEach
    public void init() {
        conditions = getDefaultConditions();
    }

    @Test
    void test() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC,"name"));
        productService.searchProducts(conditions, pageable);
    }
}

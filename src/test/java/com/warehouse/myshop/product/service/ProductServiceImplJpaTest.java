package com.warehouse.myshop.product.service;

import com.warehouse.myshop.category.mapper.CategoryMapperImpl;
import com.warehouse.myshop.product.ProductTestBase;
import com.warehouse.myshop.product.dto.FilterConditionDto;
import com.warehouse.myshop.product.dto.FilterConditionDto.NumericFilterConditionDto;
import com.warehouse.myshop.product.dto.ListProductDto;
import com.warehouse.myshop.product.mapper.ProductMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @ParameterizedTest
    @ValueSource(strings = {"=", "EQUALS", "LIKE", "~"})
    void searchProductsWhenPriceEquals100(String operation) {
        Pageable pageable = PageRequest.of(0, 2);
        NumericFilterConditionDto numericFilterConditionDto = getDefaultNumericFilterConditionDto();
        numericFilterConditionDto.setOperation(operation);
        conditions = List.of(numericFilterConditionDto);
        Double exceptedPrice = 100d;
        int exceptedSize = 1;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).anySatisfy(responseProductDto ->
                assertEquals(exceptedPrice, responseProductDto.getPrice())).hasSize(exceptedSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {">=", "GREATER_THAN_OR_EQUALS"})
    void searchProductsWhenPriceGreaterThanOrEquals300(String operation) {
        Pageable pageable = PageRequest.of(0, 2);
        NumericFilterConditionDto numericFilterConditionDto = getDefaultNumericFilterConditionDto();
        numericFilterConditionDto.setOperation(operation);
        numericFilterConditionDto.setValue(BigDecimal.valueOf(300));
        conditions = List.of(numericFilterConditionDto);
        List<Double> exceptedPrices = List.of(300d, 400d);
        final int exceptedSize = 2;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).allSatisfy(responseProductDto ->
                assertThat(responseProductDto.getPrice()).isIn(exceptedPrices)).hasSize(exceptedSize);

    }

    @ParameterizedTest
    @ValueSource(strings = {"LESS_THAN_OR_EQUALS", "<="})
    void searchProductsWhenPriceGreaterThanOrEquals200(String operation) {
        Pageable pageable = PageRequest.of(0, 2);
        NumericFilterConditionDto numericFilterConditionDto = getDefaultNumericFilterConditionDto();
        numericFilterConditionDto.setOperation(operation);
        numericFilterConditionDto.setValue(BigDecimal.valueOf(200));
        conditions = List.of(numericFilterConditionDto);
        List<Double> exceptedPrices = List.of(200d, 100d);
        int exceptedSize = 2;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).allSatisfy(responseProductDto ->
                assertThat(responseProductDto.getPrice()).isIn(exceptedPrices)).hasSize(exceptedSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {"EQUALS", "=", "LIKE", "~"})
    void searchProductsWhenDateEquals2024_4_22(String operation) {
        Pageable pageable = PageRequest.of(0, 2);
        FilterConditionDto.DateFilterConditionDto dateFilterConditionDto = getDateFilterConditionDto();
        dateFilterConditionDto.setOperation(operation);
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 22, 0, 0, 0);
        dateFilterConditionDto.setValue(localDateTime);
        conditions = List.of(dateFilterConditionDto);
        List<LocalDateTime> exceptedLocalDateTime = List.of(localDateTime);
        int exceptedSize = 1;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).anySatisfy(responseProductDto -> assertEquals(exceptedLocalDateTime.get(0).toLocalDate(),
                responseProductDto.getProductAudit().getCreationDate().toLocalDate())).hasSize(exceptedSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GREATER_THAN_OR_EQUALS", ">="})
    void searchProductsWhenDateGreaterThanOrEquals2024_4_24(String operation) {
        Pageable pageable = PageRequest.of(0, 2);
        FilterConditionDto.DateFilterConditionDto dateFilterConditionDto = getDateFilterConditionDto();
        dateFilterConditionDto.setOperation(operation);
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 4, 24, 18, 2, 54);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 4, 25, 19, 2, 54);
        dateFilterConditionDto.setValue(localDateTime1);
        conditions = List.of(dateFilterConditionDto);
        List<LocalDateTime> exceptedLocalDateTime = List.of(localDateTime1, localDateTime2);
        int exceptedSize = 2;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).allSatisfy(responseProductDto ->
                assertThat(responseProductDto.getProductAudit().getCreationDate()).isIn(exceptedLocalDateTime)).hasSize(exceptedSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {"LESS_THAN_OR_EQUALS", "<="})
    void searchProductsWhenDateLessThanOrEquals2024_4_23(String operation) {
        Pageable pageable = PageRequest.of(0, 2);
        FilterConditionDto.DateFilterConditionDto dateFilterConditionDto = getDateFilterConditionDto();
        dateFilterConditionDto.setOperation(operation);
        LocalDateTime localDateTime1 = LocalDateTime.of(2024, 4, 23, 17, 2, 54);
        LocalDateTime localDateTime2 = LocalDateTime.of(2024, 4, 22, 16, 2, 54);
        dateFilterConditionDto.setValue(localDateTime1);
        conditions = List.of(dateFilterConditionDto);
        List<LocalDateTime> exceptedLocalDateTime = List.of(localDateTime1, localDateTime2);
        int exceptedSize = 2;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).allSatisfy(responseProductDto ->
                assertThat(responseProductDto.getProductAudit().getCreationDate())
                        .isIn(exceptedLocalDateTime)).hasSize(exceptedSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {"=", "EQUALS"})
    void searchProductsWhenNameEqualsTest_name1(String operation) {
        Pageable pageable = PageRequest.of(0, 2);
        FilterConditionDto.StringFilterCondition stringFilterCondition = getDefaultStringFilterCondition();
        stringFilterCondition.setOperation(operation);
        String exceptedName = "test_name1";
        stringFilterCondition.setValue(exceptedName);
        conditions = List.of(stringFilterCondition);
        int exceptedSize = 1;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).anySatisfy(responseProductDto ->
                assertEquals(exceptedName, responseProductDto.getName())).hasSize(exceptedSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GREATER_THAN_OR_EQUALS", ">="})
    void searchProductsWhenNameGreaterThanOrEqualsTestAndSortedDescPrice(String operation) {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "price"));
        FilterConditionDto.StringFilterCondition stringFilterCondition = getDefaultStringFilterCondition();
        stringFilterCondition.setOperation(operation);
        stringFilterCondition.setValue("test");
        conditions = List.of(stringFilterCondition);
        List<String> exceptionNames = List.of("test_name3", "test_name4");
        int exceptedSize = 2;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).allSatisfy(responseProductDto ->
                assertThat(responseProductDto.getName()).isIn(exceptionNames)).hasSize(exceptedSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {"LESS_THAN_OR_EQUALS", "<="})
    void searchProductsWhenNameLessThanOrEqualsName2(String operation) {
        Pageable pageable = PageRequest.of(0, 2);
        FilterConditionDto.StringFilterCondition stringFilterCondition = getDefaultStringFilterCondition();
        stringFilterCondition.setOperation(operation);
        String exceptedName = "test_name2";
        stringFilterCondition.setValue("name2");
        conditions = List.of(stringFilterCondition);
        int exceptedSize = 1;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).anySatisfy(responseProductDto ->
                assertEquals(exceptedName, responseProductDto.getName())).hasSize(exceptedSize);
    }

    @ParameterizedTest
    @ValueSource(strings = {"LIKE", "~"})
    void searchProductsWhenNameLike_(String operation) {
        Pageable pageable = PageRequest.of(0, 10);
        FilterConditionDto.StringFilterCondition stringFilterCondition = getDefaultStringFilterCondition();
        stringFilterCondition.setOperation(operation);
        stringFilterCondition.setValue("_");
        conditions = List.of(stringFilterCondition);
        List<String> exceptionNames = List.of("test_name3", "test_name4", "test_name1", "test_name2");
        int exceptedSize = 4;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).allSatisfy(responseProductDto ->
                assertThat(responseProductDto.getName()).isIn(exceptionNames)).hasSize(exceptedSize);
    }

    @Test
    void searchProductsWhenManyConditions() {
        Pageable pageable = PageRequest.of(0, 10);
        NumericFilterConditionDto numericFilterConditionDto = getDefaultNumericFilterConditionDto();
        FilterConditionDto.DateFilterConditionDto dateFilterConditionDto = getDateFilterConditionDto();
        FilterConditionDto.StringFilterCondition stringFilterCondition = getDefaultStringFilterCondition();
        LocalDateTime localDateTime = LocalDateTime.of(2024, 4, 22, 0, 0, 0);
        dateFilterConditionDto.setValue(localDateTime);
        dateFilterConditionDto.setOperation(">=");
        numericFilterConditionDto.setValue(BigDecimal.valueOf(100));
        numericFilterConditionDto.setOperation(">=");
        stringFilterCondition.setValue("name2");
        stringFilterCondition.setOperation("~");
        conditions = List.of(numericFilterConditionDto, dateFilterConditionDto, stringFilterCondition);
        String exceptedName = "test_name2";
        int exceptedSize = 1;


        ListProductDto actualProducts = productService.searchProducts(conditions, pageable);

        assertThat(actualProducts.getProducts()).anySatisfy(responseProductDto ->
                assertEquals(exceptedName, responseProductDto.getName())).hasSize(exceptedSize);
    }
}

package com.warehouse.myshop.product.mapper;

import com.warehouse.myshop.product.dto.FilterConditionDto;
import com.warehouse.myshop.product.dto.FilterConditionDto.*;
import com.warehouse.myshop.product.model.Product;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.domain.Specification;

@Mapper
public interface ConditionMapper {
    default Specification<Product> mapToSpecification(FilterConditionDto<?> condition) {
        Class<?> conditionType = condition.getClass();
        if (conditionType == NumericFilterConditionDto.class) {
            NumericFilterConditionDto numericFilterCondition = (NumericFilterConditionDto) condition;

        }
        return null;
    }

    private void test() {

    }
}

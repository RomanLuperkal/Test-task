package com.warehouse.myshop.product.dto.condition;

import com.warehouse.myshop.product.dto.FilterConditionDto;
import com.warehouse.myshop.product.model.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class NumericFilterConditionDto extends FilterConditionDto<BigDecimal> {
    public NumericFilterConditionDto(@NotBlank String field, @NotNull BigDecimal value, String operation) {
        super(field, value, operation);
    }

    @Override
    public Specification<Product> getSpecification() {
        switch (this.operation) {
            case EQUALS:
            case LIKE:
                return (root, query, cb) -> cb.equal(root.get(this.field), this.value);
            case GREATER_THAN_OR_EQUALS:
                return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(this.field), this.value);
            case LESS_THAN_OR_EQUALS:
                return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(this.field), this.value);
            default: return null;
        }
    }
}

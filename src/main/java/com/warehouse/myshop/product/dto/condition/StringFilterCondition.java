package com.warehouse.myshop.product.dto.condition;

import com.warehouse.myshop.product.dto.FilterConditionDto;
import com.warehouse.myshop.product.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class StringFilterCondition extends FilterConditionDto<String> {

    public StringFilterCondition(String field, String value, String operation) {
        super(field, value, operation);
    }

    @Override
    public Specification<Product> getSpecification() {
        switch (this.operation) {
            case EQUALS:
                return (root, query, cb) -> cb.equal(root.get(this.field), this.value);
            case LIKE:
                return (root, query, cb) -> cb.like(root.get(this.field), "%" + this.value + "%");
            case GREATER_THAN_OR_EQUALS:
                return (root, query, cb) -> cb.like(root.get(this.field), this.value + "%");
            case LESS_THAN_OR_EQUALS:
                return (root, query, cb) -> cb.like(root.get(this.field), "%" + this.value);
            default:
                return null;
        }
    }
}

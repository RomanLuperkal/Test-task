package com.warehouse.myshop.product.dto;

import com.fasterxml.jackson.annotation.*;
import com.warehouse.myshop.product.enums.Operation;
import com.warehouse.myshop.product.dto.FilterConditionDto.*;
import com.warehouse.myshop.product.model.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "field", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericFilterConditionDto.class, name = "price"),
        @JsonSubTypes.Type(value = DateFilterConditionDto.class, name = "createdAt"),
        @JsonSubTypes.Type(value = StringFilterCondition.class, name = "name")
})

@Setter
@Getter
public abstract class FilterConditionDto<T> {
    protected String field;
    protected T value;
    protected Operation operation;

    abstract Specification<Product> getSpecification();


    public void setOperation(String operation) {
        this.operation = Operation.fromString(operation);
    }

    public static class NumericFilterConditionDto extends  FilterConditionDto<BigDecimal>{
        @Override
        Specification<Product> getSpecification() {
            switch (this.operation) {
                case EQUALS:
                case LIKE:
                    return (root, query, cb) -> cb.equal(root.get(this.field), this.value);
                case GREATER_THAN_OR_EQUALS:
                    return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(this.field), this.value);
                case LESS_THAN_OR_EQUALS:
                    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(this.field), this.value);
            }
        }
    }

    public static class DateFilterConditionDto extends FilterConditionDto<LocalDateTime> {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd.MM.yyyy")
        public LocalDateTime getValue() {
            return super.getValue();
        }
    }

    public static class StringFilterCondition extends FilterConditionDto<String> {
    }
}

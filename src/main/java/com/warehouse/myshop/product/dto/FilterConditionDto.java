package com.warehouse.myshop.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.warehouse.myshop.product.dto.FilterConditionDto.DateFilterConditionDto;
import com.warehouse.myshop.product.dto.FilterConditionDto.NumericFilterConditionDto;
import com.warehouse.myshop.product.dto.FilterConditionDto.StringFilterCondition;
import com.warehouse.myshop.product.enums.Operation;
import com.warehouse.myshop.product.model.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "field", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericFilterConditionDto.class, name = "price"),
        @JsonSubTypes.Type(value = DateFilterConditionDto.class, name = "creationDate"),
        @JsonSubTypes.Type(value = StringFilterCondition.class, name = "name")
})

@Setter
@Getter
public abstract class FilterConditionDto<T> {

    protected String field;
    protected T value;
    protected Operation operation;

    public abstract Specification<Product> getSpecification();


    public void setOperation(String operation) {
        this.operation = Operation.fromString(operation);
    }

    public static class NumericFilterConditionDto extends  FilterConditionDto<BigDecimal>{
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

    public static class DateFilterConditionDto extends FilterConditionDto<LocalDateTime> {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        public LocalDateTime getValue() {
            return super.getValue();
        }

        @Override
        public Specification<Product> getSpecification() {
            switch (this.operation) {
                case EQUALS:
                case LIKE:
                    return (root, query, cb) -> cb.equal(root.get("productAudit").get(this.field), this.value.toLocalDate());
                case GREATER_THAN_OR_EQUALS:
                    return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("productAudit").get(this.field), this.value.toLocalDate());
                case LESS_THAN_OR_EQUALS:
                    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("productAudit").get(this.field), this.value.toLocalDate());
                default: return null;
            }
        }
    }

    public static class StringFilterCondition extends FilterConditionDto<String> {
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
                default:return null;
            }
        }
    }
}

package com.warehouse.myshop.product.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.warehouse.myshop.product.dto.condition.DateFilterConditionDto;
import com.warehouse.myshop.product.dto.condition.NumericFilterConditionDto;
import com.warehouse.myshop.product.dto.condition.StringFilterCondition;
import com.warehouse.myshop.product.enums.Operation;
import com.warehouse.myshop.product.model.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "field", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericFilterConditionDto.class, name = "price"),
        @JsonSubTypes.Type(value = DateFilterConditionDto.class, name = "creationDate"),
        @JsonSubTypes.Type(value = StringFilterCondition.class, name = "name")
})

@Getter
@RequiredArgsConstructor
public abstract class FilterConditionDto<T> {

    @NotBlank
    protected final String field;
    @NotNull
    protected final T value;
    protected final Operation operation;

    public FilterConditionDto(String field, T value, String operation) {
        this.field = field;
        this.value = value;
        this.operation = Operation.fromString(operation);
    }

    public abstract Specification<Product> getSpecification();


   /* public void setOperation(String operation) {
        this.operation = Operation.fromString(operation);
    }*/






}

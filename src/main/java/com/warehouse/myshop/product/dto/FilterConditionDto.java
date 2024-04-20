package com.warehouse.myshop.product.dto;

import com.fasterxml.jackson.annotation.*;
import com.warehouse.myshop.product.enums.Operation;
import com.warehouse.myshop.product.dto.FilterConditionDto.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "field", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumericFilterConditionDto.class, name = "price"),
        @JsonSubTypes.Type(value = DateFilterConditionDto.class, name = "createdAt"),
        @JsonSubTypes.Type(value = StringFilterCondition.class, name = "name")
})

@Setter
@Getter
public abstract class FilterConditionDto<T> {
    private String field;
    private T value;
    private Operation operation;


    public void setOperation(String operation) {
        this.operation = Operation.fromString(operation);
    }

    public static class NumericFilterConditionDto extends  FilterConditionDto<BigDecimal>{}

    public static class DateFilterConditionDto extends FilterConditionDto<LocalDateTime> {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd.MM.yyyy")
        public LocalDateTime getValue() {
            return super.getValue();
        }
    }

    public static class StringFilterCondition extends FilterConditionDto<String> {
    }
}

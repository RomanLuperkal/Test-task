package com.warehouse.myshop.product.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.util.Date;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "field")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FilterConditionDto.NumericFilterConditionDto.class, name = "price"),
        @JsonSubTypes.Type(value = FilterConditionDto.DateFilterConditionDto.class, name = "creationDate"),
        @JsonSubTypes.Type(value = FilterConditionDto.StringFilterCondition.class, name = "name")
})
public class FilterConditionDto {
    private String field;

    public static class NumericFilterConditionDto extends  FilterConditionDto{
        private Double value;
    }

    public static class DateFilterConditionDto extends FilterConditionDto {
        private Date value;
    }

    public static class StringFilterCondition extends FilterConditionDto {
        private String value;
    }
}

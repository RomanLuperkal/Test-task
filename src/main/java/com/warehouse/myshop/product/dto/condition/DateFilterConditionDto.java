package com.warehouse.myshop.product.dto.condition;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warehouse.myshop.product.dto.FilterConditionDto;
import com.warehouse.myshop.product.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateFilterConditionDto extends FilterConditionDto<LocalDateTime> {

    public DateFilterConditionDto(String field,
                                  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime value,
                                  String operation) {
        super(field, value, operation);
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getValue() {
        return super.getValue();
    }

    @Override
    public Specification<Product> getSpecification() {
        switch (this.operation) {
            case EQUALS:
            case LIKE:
                return (root, query, cb) -> cb.equal(root.get("productAudit").get(this.field).as(LocalDate.class), this.value.toLocalDate());
            case GREATER_THAN_OR_EQUALS:
                return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("productAudit").get(this.field), this.value);
            case LESS_THAN_OR_EQUALS:
                return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("productAudit").get(this.field), this.value);
            default: return null;
        }
    }
}

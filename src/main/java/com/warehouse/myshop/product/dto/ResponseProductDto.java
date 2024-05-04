package com.warehouse.myshop.product.dto;

import com.warehouse.myshop.category.dto.CategoryDtoResp;
import com.warehouse.myshop.product.audit.ProductAudit;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProductDto {
    private UUID uuid;
    private String name;
    private String articleNumber;
    private String description;
    private CategoryDtoResp category;
    private BigDecimal price;
    private Integer quantity;
    private ProductAudit productAudit;
    private String currency;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseProductDto that = (ResponseProductDto) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}

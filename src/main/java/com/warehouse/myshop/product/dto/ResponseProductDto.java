package com.warehouse.myshop.product.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder
@Data
public class ResponseProductDto {
    private UUID uuid;
    private String name;
    private String articleNumber;
    private String description;
    private Long categoryId;
    private Double price;
    private Integer quantity;
    private LocalDateTime lastUpdate;
    private LocalDateTime creationDate;

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

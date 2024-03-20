package com.warehouse.myshop.category.dto;

import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CategoryDtoResp {
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDtoResp that = (CategoryDtoResp) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

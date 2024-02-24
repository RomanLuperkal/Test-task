package com.warehouse.myshop.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NewCategoryDto {
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Pattern(regexp = "^[^ ].*[^ ]$", message = "Invalid name")
    private String name;
}

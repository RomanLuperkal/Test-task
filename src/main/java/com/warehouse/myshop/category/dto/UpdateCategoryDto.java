package com.warehouse.myshop.category.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class UpdateCategoryDto {
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Pattern(regexp = "^[^ ].*[^ ]$", message = "Invalid name")
    private String name;
}

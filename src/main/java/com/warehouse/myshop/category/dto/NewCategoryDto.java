package com.warehouse.myshop.category.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Setter
@Getter
public class NewCategoryDto {
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Pattern(regexp = "^[^ ].*[^ ]$", message = "Invalid name")
    private String name;
}

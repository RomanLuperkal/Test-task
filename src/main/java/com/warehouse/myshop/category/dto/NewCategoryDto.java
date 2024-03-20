package com.warehouse.myshop.category.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Pattern(regexp = "^[^ ].*[^ ]$", message = "Invalid name")
    private String name;
}

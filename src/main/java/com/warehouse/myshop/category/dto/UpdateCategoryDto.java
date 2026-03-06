package com.warehouse.myshop.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryDto {
    @NotBlank(message = "Недопустимое имя")
    @Pattern(regexp = "^[^ ].*[^ ]$", message = "Недопустимое имя")
    private String name;
}

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
    @NotBlank(message = "Недопустимое имя")
    @Pattern(regexp = "^[^ ].*[^ ]$", message = "Недопустимое имя")
    private String name;
}

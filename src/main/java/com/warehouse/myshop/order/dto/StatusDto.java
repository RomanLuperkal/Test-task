package com.warehouse.myshop.order.dto;

import com.warehouse.myshop.order.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDto {
    @NotNull
    private Status status;
}

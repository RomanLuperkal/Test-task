package com.warehouse.myshop.order.dto;

import com.warehouse.myshop.order.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StatusDto {
    @NotNull
    private Status status;
}

package com.warehouse.myshop.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CustomerInfo {
    Long id;

    String accountNumber;

    String email;

    String inn;
}

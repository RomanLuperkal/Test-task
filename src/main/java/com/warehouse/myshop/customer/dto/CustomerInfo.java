package com.warehouse.myshop.customer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomerInfo {
    UUID id;

    String accountNumber;

    String email;

    String inn;
}

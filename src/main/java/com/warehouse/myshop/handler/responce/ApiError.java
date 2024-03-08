package com.warehouse.myshop.handler.responce;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    private String message;
    private String reason;
    private String status;
    private LocalDateTime time;
}

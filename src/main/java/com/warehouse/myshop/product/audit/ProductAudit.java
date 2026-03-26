package com.warehouse.myshop.product.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class ProductAudit {
    @Column(name = "last_update")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd.MM.yyyy")
    private LocalDateTime lastUpdate;
    @Column(nullable = false, name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd.MM.yyyy")
    private LocalDateTime creationDate = LocalDateTime.now();
}

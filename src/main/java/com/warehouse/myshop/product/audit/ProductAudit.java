package com.warehouse.myshop.product.audit;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class ProductAudit {
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    @Column(nullable = false, name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();
}

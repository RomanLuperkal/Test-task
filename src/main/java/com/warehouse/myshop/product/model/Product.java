package com.warehouse.myshop.product.model;

import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.product.audit.ProductAudit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue
    private UUID uuid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, name = "article_number", unique = true)
    private String articleNumber;
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Integer quantity;
    @Embedded
    private ProductAudit productAudit = new ProductAudit();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return getUuid() != null && Objects.equals(getUuid(), product.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}

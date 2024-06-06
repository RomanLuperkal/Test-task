package com.warehouse.myshop.product.model;

import com.warehouse.myshop.category.model.Category;
import com.warehouse.myshop.product.audit.ProductAudit;
import com.warehouse.myshop.productimage.model.Image;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer quantity;
    @Embedded
    private ProductAudit productAudit = new ProductAudit();
    @Column(nullable = false, name = "is_available")
    private Boolean isAvailable = false;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images;

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

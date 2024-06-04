package com.warehouse.myshop.productimage.model;

import com.warehouse.myshop.product.model.Product;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_uuid")
    private UUID imageUuid;

    @ManyToOne
    @JoinColumn(name = "product_uuid")
    private Product product;

    @Column(name = "image_name")
    private String imageName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Image image = (Image) o;
        return imageUuid != null && Objects.equals(imageUuid, image.imageUuid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

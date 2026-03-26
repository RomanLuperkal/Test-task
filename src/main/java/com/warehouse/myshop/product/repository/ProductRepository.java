package com.warehouse.myshop.product.repository;

import com.warehouse.myshop.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Product> findAll();

    @Query("SELECT " +
            "CASE WHEN COUNT(p) = :size THEN true ELSE false END " +
            "FROM Product p " +
            "WHERE p.uuid in :uuids")
    Boolean isExistsProducts(@Param("uuids") Set<UUID> uuids, @Param("size") Long size);

    List<Product> findAllByUuidIn(Set<UUID> ids);
}

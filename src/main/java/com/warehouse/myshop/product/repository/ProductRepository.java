package com.warehouse.myshop.product.repository;

import com.warehouse.myshop.product.model.Product;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Product> findAll();

    Boolean existsAllByUuidIn(List<UUID> uuids);

    Boolean existsAllByUuidInAndIsAvailableIsTrue(List<UUID> uuids);

    @Query("SELECT " +
            "CASE WHEN COUNT(p) = :size THEN true ELSE false END " +
            "FROM Product p " +
            "WHERE p.uuid in :uuids")
    Boolean isExistsProducts(@Param("uuids") List<UUID> uuids, @Param("size") Long size);

    //productRepository.isSufficientProductInStock(List.of(UUID.fromString("7f43c238-f849-4a1a-9e48-392f21e1d67e"), UUID.randomUUID()), productIds.size())
    @Transactional(propagation = Propagation.MANDATORY)
    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.uuid = :uuid")
    void updateProductQuantityByUuid(@Param("uuid") UUID uuid, @Param("quantity") Integer quantity);

    @Query("SELECT p.price FROM Product  p WHERE p.uuid = :uuid")
    BigDecimal getPriceByUuid(@Param("uuid") UUID uuid);
}

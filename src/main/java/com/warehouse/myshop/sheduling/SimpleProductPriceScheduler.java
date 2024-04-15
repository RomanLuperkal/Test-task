package com.warehouse.myshop.sheduling;

import com.warehouse.myshop.product.model.Product;
import com.warehouse.myshop.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SimpleProductPriceScheduler {

    @Autowired
    ProductRepository productRepository;

    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @Transactional
    public void scheduleFixedDelayTask() {
        log.info("Start simple scheduler");
        List<Product> updatedProducts = productRepository.findAll().stream().peek(p -> p.setPrice(p.getPrice() + 10)).
                collect(Collectors.toList());
        productRepository.saveAll(updatedProducts);
        log.info("End simple scheduler");
    }
}

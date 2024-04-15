package com.warehouse.myshop.sheduling;

import com.warehouse.myshop.anotation.TimeTrack;
import com.warehouse.myshop.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class SimpleProductPriceScheduler {

    @Autowired
    private ProductRepository productRepository;
    @Value("${app.scheduling.priceIncrease}")
    private Double priceIncrease;


    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @Transactional
    @TimeTrack
    public void scheduleFixedDelayTask() {
        log.info("Start simple scheduler");
        productRepository.findAll().forEach(p -> p.setPrice(p.getPrice() + priceIncrease));
        log.info("End simple scheduler");
    }
}

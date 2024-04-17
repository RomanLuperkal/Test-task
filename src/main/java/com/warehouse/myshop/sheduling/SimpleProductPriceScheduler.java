package com.warehouse.myshop.sheduling;

import com.warehouse.myshop.anotation.TimeTrack;
import com.warehouse.myshop.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class SimpleProductPriceScheduler {

    @Autowired
    private ProductRepository productRepository;
    @Value("${app.scheduling.priceIncrease}")
    private BigDecimal priceIncrease;


    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @Transactional
    @TimeTrack
    public void scheduleFixedDelayTask() {
        log.info("Start simple scheduler");
        productRepository.findAll().forEach(p -> {
            final BigDecimal actualPrice = p.getPrice();
            final BigDecimal ONE_HUNDRED_PERCENT = BigDecimal.valueOf(100L);
            final BigDecimal increasedPrice = actualPrice.add(actualPrice.multiply(priceIncrease)
                    .divide(ONE_HUNDRED_PERCENT, 2, RoundingMode.HALF_UP));

            p.setPrice(increasedPrice);
        });
        log.info("End simple scheduler");
    }
}

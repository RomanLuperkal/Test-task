package com.warehouse.myshop.sheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class OptimizedProductPriceScheduler {
    @Value("${app.scheduling.priceIncrease}")
    private Double priceIncrease;

    @Value("${app.scheduling.enabled}")
    private boolean schedulingEnabled;

    @Value("${app.scheduling.optimization}")
    private boolean schedulingOptimization;


    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    //@Transactional
    public void scheduleFixedDelayTask() {
        log.info("Start optimized scheduler");
        System.out.println(schedulingEnabled + " and " + schedulingOptimization);
        log.info("End optimized scheduler");
    }
}

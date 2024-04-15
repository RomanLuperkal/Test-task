package com.warehouse.myshop.sheduling;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

public class SimpleProductPriceScheduler {

    @Scheduled(fixedDelay = 10000)
    public void scheduleFixedDelayTask() {
        System.out.println(LocalDateTime.now());
    }
}

package com.warehouse.myshop.configuration;

import com.warehouse.myshop.sheduling.SimpleProductPriceScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {

    @Bean
    public SimpleProductPriceScheduler getSimpleScheduler() {
        return new SimpleProductPriceScheduler();
    }
}

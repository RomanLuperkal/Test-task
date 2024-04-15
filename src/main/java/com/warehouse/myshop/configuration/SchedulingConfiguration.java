package com.warehouse.myshop.configuration;

import com.warehouse.myshop.sheduling.SimpleProductPriceScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {

    @Bean
    @Profile("default")
    @ConditionalOnProperty(value = "app.scheduling.enabled", havingValue = "true")
    public SimpleProductPriceScheduler getSimpleScheduler() {
        return new SimpleProductPriceScheduler();
    }
}

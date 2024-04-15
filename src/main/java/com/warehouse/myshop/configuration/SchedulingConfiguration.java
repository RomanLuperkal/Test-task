package com.warehouse.myshop.configuration;

import com.warehouse.myshop.sheduling.OptimizedProductPriceScheduler;
import com.warehouse.myshop.sheduling.SimpleProductPriceScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfiguration {

    @Bean
    @Profile("default")
    @ConditionalOnExpression("${app.scheduling.enabled} && ${app.scheduling.optimization}")
    public OptimizedProductPriceScheduler getOptimizedScheduler() {
        return new OptimizedProductPriceScheduler();
    }
    @Bean
    @Profile("default")
    @ConditionalOnExpression("${app.scheduling.enabled} && !${app.scheduling.optimization}")
    public SimpleProductPriceScheduler getSimpleScheduler() {
        return new SimpleProductPriceScheduler();
    }


}

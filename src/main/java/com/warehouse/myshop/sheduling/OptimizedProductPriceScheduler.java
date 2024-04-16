package com.warehouse.myshop.sheduling;

import com.warehouse.myshop.anotation.TimeTrack;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;

@Slf4j
public class OptimizedProductPriceScheduler {
    @Value("${app.scheduling.priceIncrease}")
    private Double priceIncrease;

    @PersistenceContext
    private EntityManager entityManager;
    private final String updateQuery = "UPDATE product set price = price + ?";

    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @TimeTrack
    public void scheduleFixedDelayTask() {
        log.info("Start optimized scheduler");
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)){
                statement.setDouble(1, priceIncrease);
            }
        });
        log.info("End optimized scheduler");
    }
}

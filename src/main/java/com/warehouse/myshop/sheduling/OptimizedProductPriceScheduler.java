package com.warehouse.myshop.sheduling;

import com.warehouse.myshop.anotation.TimeTrack;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            int pageSize = 1000;
            try {
                connection.setAutoCommit(false);


                boolean moreRows = true;
                int offset = 0;
                while (moreRows) {
                    List<UUID> batch = fetchUuids(connection, pageSize, offset);
                    if (batch.isEmpty()) {
                        moreRows = false;
                    } else {
                        updatePrices(connection, batch);
                    }
                    offset += pageSize;
                    System.out.println("Обновлено " + offset + " строк");
                }
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        log.info("End optimized scheduler");
    }

    private static List<UUID> fetchUuids(Connection conn, int pageSize, int offset) throws SQLException {
        List<UUID> uuids = new ArrayList<>();
        String sql = "SELECT uuid FROM product ORDER BY uuid LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, pageSize);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                uuids.add(UUID.fromString(rs.getString("uuid")));
            }
        }
        return uuids;
    }

    private static void updatePrices(Connection conn, List<UUID> uuids) throws SQLException {
        String updateSQL = "UPDATE product SET price = price + 10 WHERE uuid = ?";
        try (PreparedStatement statement = conn.prepareStatement(updateSQL)) {
            for (UUID uuid : uuids) {
                statement.setObject(1, uuid);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}

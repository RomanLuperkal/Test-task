package com.warehouse.myshop.sheduling;

import com.warehouse.myshop.anotation.TimeTrack;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class OptimizedProductPriceScheduler {
    @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncrease}\")}")
    private BigDecimal priceIncrease;
    @Value("${app.scheduling.exclusive-lock}")
    private Boolean isLock;

    @PersistenceContext
    private EntityManager entityManager;
    final String filePath = "src/main/resources/updatedProducts.txt";

    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @TimeTrack
    public void scheduleFixedDelayTask() {
        log.info("Start optimized scheduler");
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            long offset = 0L;
            long updatedRows = 0L;
            long pageSize = calculatePageSize(connection);

            try {
                connection.setAutoCommit(false);
                if (isLock)
                    lockTable(connection);

                while (true) {
                    List<UUID> batch = fetchUuids(connection, pageSize, offset);
                    if (batch.isEmpty()) {
                        break;
                    }

                    updatedRows += updatePrices(connection, batch).length;

                    offset += pageSize;
                    System.out.println("Обновлено " + updatedRows + " строк");
                }

                connection.commit();
                saveToFile(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        log.info("End optimized scheduler");
    }

    private List<UUID> fetchUuids(Connection conn, long pageSize, long offset) throws SQLException {
        List<UUID> uuids = new ArrayList<>();
        String sql = "SELECT uuid FROM product ORDER BY uuid LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, pageSize);
            statement.setLong(2, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                uuids.add(UUID.fromString(rs.getString("uuid")));
            }
        }
        return uuids;
    }

    private int[] updatePrices(Connection conn, List<UUID> uuids) throws SQLException {
        String updateSQL = "UPDATE product SET price = price + (price / 100) * " + priceIncrease + " WHERE uuid = ?";
        try (PreparedStatement statement = conn.prepareStatement(updateSQL)) {
            for (UUID uuid : uuids) {
                statement.setObject(1, uuid);
                statement.addBatch();
            }
            return statement.executeBatch();
        }
    }

    private long calculatePageSize(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareCall("SELECT count(*) FROM product")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Math.round(resultSet.getInt(1) * 0.1);
            }
            return 1;
        }
    }

    private void saveToFile(Connection connection) throws SQLException {
        File file = new File(filePath);
        ResultSet rs;
        final String productFields = "uuid, name, article_number, description, " +
                "category_id, price, quantity, last_update, creation_date\n";
        final String format = "%s, %s, %s, %s, %d, %.2f, %d, %s, %s\n";

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM product");
             FileWriter writer = new FileWriter(file, true)){
            if (file.length() == 0) {
                writer.write(productFields);
            }
            rs = statement.executeQuery();
            while (rs.next()) {
                writer.write(String.format(format, rs.getObject("uuid"),
                        rs.getString("name"),
                        rs.getString("article_number"), rs.getString("description"),
                        rs.getLong("category_id"), rs.getBigDecimal("price"),
                        rs.getLong("quantity"), rs.getString("last_update"),
                        rs.getString("creation_date")));
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка при записи в файл: " + e.getMessage());
        }
    }

    private void lockTable(Connection connection) throws SQLException {
        String lockTableQuery = "LOCK TABLE product IN ACCESS EXCLUSIVE MODE";
        try (PreparedStatement statement = connection.prepareStatement(lockTableQuery)){
            statement.execute();
        }
    }
}


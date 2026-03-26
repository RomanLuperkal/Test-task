package com.warehouse.myshop.sheduling;

import com.warehouse.myshop.anotation.TimeTrack;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class OptimizedProductPriceScheduler {
    @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncrease}\")}")
    private BigDecimal priceIncrease;
    @Value("${app.scheduling.exclusive-lock}")
    private Boolean isLock;
    private final String QUERY = "UPDATE product SET price = price * (1 + ? /100) RETURNING *";

    @PersistenceContext
    private EntityManager entityManager;
    final String filePath = "src/main/java/com/warehouse/myshop/file/updatedProducts.txt";

    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @TimeTrack
    public void scheduleFixedDelayTask() {
        log.info("Start optimized scheduler");
        Session session = entityManager.unwrap(Session.class);
        try (session) {
            session.doWork(connection -> {
                try {
                    connection.setAutoCommit(false);
                    if (isLock)
                        lockTable(connection);

                    final PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
                    preparedStatement.setBigDecimal(1, priceIncrease);

                    final ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        saveToFile(resultSet);
                    }
                    connection.commit();

                } catch (Exception e) {
                    connection.rollback();
                    e.printStackTrace();
                }
            });
        }
        log.info("End optimized scheduler");
    }

    private void saveToFile(ResultSet rs) throws SQLException {
        File file = new File(filePath);
        final String productFields = "uuid, name, article_number, description, " +
                "category_id, price, quantity, last_update, creation_date\n";
        final String format = "%s, %s, %s, %s, %d, %.2f, %d, %s, %s\n";

        try (FileWriter writer = new FileWriter(file, true)) {
            if (file.length() == 0) {
                writer.write(productFields);
            }
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
        try (PreparedStatement statement = connection.prepareStatement(lockTableQuery)) {
            statement.execute();
        }
    }
}


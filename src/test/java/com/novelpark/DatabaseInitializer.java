package com.novelpark;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseInitializer {

  private static final String TRUNCATE_QUERY = "TRUNCATE TABLE %s";
  private static final String ALTER_AUTO_INCREMENT_INIT_QUERY = "ALTER TABLE %s AUTO_INCREMENT = 1";

  private final EntityManager entityManager;
  private final DataSource dataSource;
  private final Set<String> tableNames;

  public DatabaseInitializer(EntityManager entityManager, DataSource dataSource) {
    this.entityManager = entityManager;
    this.dataSource = dataSource;
    tableNames = new HashSet<>();
  }

  @PostConstruct
  public void afterPropertiesSet() {
    try {
      DatabaseMetaData metaData = dataSource
          .getConnection()
          .getMetaData();
      ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
      while (tables.next()) {
        String tableName = tables.getString("TABLE_NAME");
        tableNames.add(tableName);
      }
    } catch (Exception e) {
      throw new RuntimeException("테이블 이름을 불러올 수 없습니다.");
    }
  }

  @Transactional
  public void truncateTables() {
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
    for (String tableName : tableNames) {
      truncateTable(tableName);
    }
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
  }

  private void truncateTable(final String tableName) {
    entityManager
        .createNativeQuery(String.format(TRUNCATE_QUERY, tableName))
        .executeUpdate();
    entityManager
        .createNativeQuery(String.format(ALTER_AUTO_INCREMENT_INIT_QUERY, tableName))
        .executeUpdate();
  }
}

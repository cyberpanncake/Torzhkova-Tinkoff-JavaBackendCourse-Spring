package edu.java.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.simple.JdbcClient;
import javax.sql.DataSource;

public class SimpleMigrationTest extends IntegrationTest {
    private final JdbcClient jdbcClient;

    public SimpleMigrationTest() {
        DataSource source = DataSourceBuilder.create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build();
        this.jdbcClient = JdbcClient.create(source);
    }

    @Test
    void migrationBySimpleSqlQueryTest() {
        String sql = "select * from chat";
        Assertions.assertDoesNotThrow(() -> jdbcClient.sql(sql).query().listOfRows());
    }
}

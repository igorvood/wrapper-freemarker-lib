package ru.vood.freemarker.ext.processor;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NativeSqlFtlProcessor extends SpringFtlProcessor {

    public NativeSqlFtlProcessor(
            String jdbcDriver,
            String url,
            String username,
            String password) throws SQLException, ClassNotFoundException {
        super(getJdbcOperations(jdbcDriver, url, username, password));
    }

    @NotNull
    private static JdbcOperations getJdbcOperations(String jdbcDriver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(jdbcDriver);
        final Driver driver = DriverManager.getDriver(url);
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, url, username, password);
        return new JdbcTemplate(dataSource);
    }
}
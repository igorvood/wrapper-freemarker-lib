package ru.vood.freemarker.ext.processor

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import java.sql.DriverManager

class NativeSqlFtlProcessor(
        jdbcDriver: String,
        url: String,
        username: String,
        password: String) : SpringFtlProcessor(getJdbcOperations(jdbcDriver, url, username, password)) {
    companion object {

        private fun getJdbcOperations(jdbcDriver: String, url: String, username: String, password: String): JdbcOperations {
            Class.forName(jdbcDriver)
            val driver = DriverManager.getDriver(url)
            val dataSource = SimpleDriverDataSource(driver, url, username, password)
            return JdbcTemplate(dataSource)
        }
    }
}
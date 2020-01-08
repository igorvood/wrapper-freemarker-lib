package ru.vood.freemarker.ext.processor

import freemarker.template.Template
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import java.sql.DriverManager

class NativeSqlFtlProcessor(
        private val jdbcDriver: String,
        private val url: String,
        private val username: String,
        private val password: String) : SimpleFtlProcessor() {

    override fun process(template: Template, vararg args: Any?): String {
        val jdbcOperations = getJdbcOperations(jdbcDriver, url, username, password)
        val springFtlProcessor = SpringFtlProcessor(jdbcOperations)
        registerSharedVar("default_connection", springFtlProcessor.getDefaultConnectionMethod)
        this.sharedVariableNames.stream()
                .map { Pair(it as String, this.getSharedVariable(it)) }
                .forEach { springFtlProcessor.registerSharedVar(it.first, it.second) }
        val process = springFtlProcessor.process(template, *args)
//        jdbcOperations.dataSource.connection.close()
        return process
    }

    companion object {

        private fun getJdbcOperations(jdbcDriver: String, url: String, username: String, password: String): JdbcTemplate {
            Class.forName(jdbcDriver)
            val driver = DriverManager.getDriver(url)
            val dataSource = SimpleDriverDataSource(driver, url, username, password)
            return JdbcTemplate(dataSource)
        }
    }
}
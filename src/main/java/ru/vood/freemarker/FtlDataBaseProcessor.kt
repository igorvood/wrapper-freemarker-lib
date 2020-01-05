package ru.vood.freemarker

import freemarker.template.Configuration
import oracle.jdbc.driver.OracleDriver
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SimpleDriverDataSource
import ru.vood.freemarker.ext.sql.SqlFtlProcessor
import java.io.File
import java.sql.Driver
import java.sql.DriverManager
import javax.sql.DataSource

class FtlDataBaseProcessor(
        val cfg: Configuration,
        val jdbcDriver: String,
        val url: String,
        private val user: String,
        private val password: String) : TemplateProcessor {

    lateinit var sqlFtlProcessor: SqlFtlProcessor

    fun qwe() {
        /* регистрация драйвера Oracle */
        Class.forName("oracle.jdbc.driver.OracleDriver")
        val c = DriverManager.getConnection(
                "jdbc:oracle:thin:@prod1:1521:finprod",
                "user", "user_passwd")

        val driver: Driver = OracleDriver()
        val dataSource: DataSource = SimpleDriverDataSource(driver, url, user, password)

        val jdbcTemplate: JdbcOperations = JdbcTemplate(dataSource)
        sqlFtlProcessor = SqlFtlProcessor(jdbcTemplate)

    }

    override fun processFile(file: File, args: Array<Any?>?): String {
        val forName = Class.forName(jdbcDriver)
        val c = DriverManager.getConnection(url, user, password)
        return ""
    }
}
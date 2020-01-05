package ru.vood.freemarker

import org.springframework.jdbc.core.JdbcOperations
import ru.vood.freemarker.ext.sql.SqlFtlProcessor
import java.io.File

class JdbcOperationsFtlProcessor(jdbcOperations: JdbcOperations) : TemplateProcessor {

    private val sqlFtlProcessor: SqlFtlProcessor = SqlFtlProcessor(jdbcOperations)

    override fun processFile(file: File, args: Array<Any?>?): String {
        return sqlFtlProcessor.process(sqlFtlProcessor.getTemplate(file.absolutePath), args)
    }
}
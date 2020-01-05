package ru.vood.freemarker

import org.springframework.jdbc.core.JdbcOperations
import ru.vood.freemarker.ext.processor.SpringFtlProcessor
import java.io.File

class JdbcOperationsFtlProcessor(jdbcOperations: JdbcOperations) : TemplateProcessor {

    private val sqlFtlProcessor: SpringFtlProcessor = SpringFtlProcessor(jdbcOperations)

    override fun processFile(file: File, args: Array<Any?>?): String {
        return sqlFtlProcessor.process(sqlFtlProcessor.getTemplate(file.absolutePath), args)
    }
}
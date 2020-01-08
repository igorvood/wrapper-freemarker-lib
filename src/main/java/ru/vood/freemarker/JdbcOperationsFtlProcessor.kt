package ru.vood.freemarker

import org.springframework.jdbc.core.JdbcTemplate
import ru.vood.freemarker.ext.processor.SpringFtlProcessor

class JdbcOperationsFtlProcessor(jdbcOperations: JdbcTemplate) : TemplateProcessor {

    private val sqlFtlProcessor: SpringFtlProcessor = SpringFtlProcessor(jdbcOperations)

    override fun processFile(fileName: String, vararg args: Any?): String {
        return sqlFtlProcessor.process(sqlFtlProcessor.getTemplate(fileName), args)
    }

    override fun registerSharedVar(name: String, `val`: Any) {
        sqlFtlProcessor.setSharedVariable(name, `val`)
    }

}
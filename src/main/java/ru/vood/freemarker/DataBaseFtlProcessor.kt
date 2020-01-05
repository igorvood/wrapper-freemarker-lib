package ru.vood.freemarker

import ru.vood.freemarker.ext.processor.NativeSqlFtlProcessor
import ru.vood.freemarker.ext.processor.SpringFtlProcessor
import java.io.File

class DataBaseFtlProcessor(
        jdbcDriver: String,
        url: String,
        user: String,
        password: String) : TemplateProcessor {

    var nativeSqlFtlProcessor: SpringFtlProcessor = NativeSqlFtlProcessor(jdbcDriver, url, user, password)

    override fun processFile(file: File, args: Array<Any?>?): String {
        return nativeSqlFtlProcessor.process(nativeSqlFtlProcessor.getTemplate(file.absolutePath), args)
    }
}
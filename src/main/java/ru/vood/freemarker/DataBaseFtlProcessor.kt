package ru.vood.freemarker

import ru.vood.freemarker.ext.processor.NativeSqlFtlProcessor
import java.io.File

class DataBaseFtlProcessor(
        jdbcDriver: String,
        url: String,
        user: String,
        password: String) : TemplateProcessor {

    var nativeSqlFtlProcessor: NativeSqlFtlProcessor = NativeSqlFtlProcessor(jdbcDriver, url, user, password)

    override fun processFile(file: File, args: Array<Any?>?): String {
        return nativeSqlFtlProcessor.process(nativeSqlFtlProcessor.getTemplate(file.absolutePath), args)
    }
}
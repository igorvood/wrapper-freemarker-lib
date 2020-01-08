package ru.vood.freemarker

import ru.vood.freemarker.ext.processor.NativeSqlFtlProcessor

class DataBaseFtlProcessor(
        jdbcDriver: String,
        url: String,
        user: String,
        password: String) : TemplateProcessor {

    var nativeSqlFtlProcessor: NativeSqlFtlProcessor = NativeSqlFtlProcessor(jdbcDriver, url, user, password)

    override fun processFile(fileName: String, vararg args: Any?): String {
        return nativeSqlFtlProcessor.process(nativeSqlFtlProcessor.getTemplate(fileName), args)
    }

    override fun registerSharedVar(name: String, `val`: Any) {
        nativeSqlFtlProcessor.setSharedVariable(name, `val`)
    }
}
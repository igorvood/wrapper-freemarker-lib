package ru.vood.freemarker

import freemarker.cache.FileTemplateLoader
import freemarker.template.Configuration
import java.io.File
import java.io.StringWriter
import java.util.*

class FtlProcessorImpl() : FtlProcessor {

    val cfg = Configuration(Configuration.VERSION_2_3_29)

    override fun processFile(fileName: String, args: Array<Any?>?): String {
        val ftl1 = FileTemplateLoader(File(fileName))
        cfg.templateLoader = ftl1

        val data: MutableMap<String, Any> = HashMap()
        if (args != null)
            data["args"] = args
        val temlate = cfg.getTemplate(fileName)
        val stringWriter = StringWriter()
        temlate.process(data, stringWriter)
        return ""
    }
}
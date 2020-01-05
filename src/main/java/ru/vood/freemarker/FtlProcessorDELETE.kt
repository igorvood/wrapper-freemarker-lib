package ru.vood.freemarker

import freemarker.cache.FileTemplateLoader
import freemarker.template.Configuration
import java.io.File
import java.io.StringWriter
import java.util.*

class FtlProcessorDELETE(val cfg: Configuration) : TemplateProcessor {

    constructor() : this(Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS))

    override fun processFile(file: File, args: Array<Any?>?): String {
        if (!file.exists())
            throw IllegalStateException("File ${file.absolutePath} doesn't exists")
        val ftl = FileTemplateLoader(File(file.parent))
        cfg.templateLoader = ftl

        val data: MutableMap<String, Any> = HashMap()
        if (args != null)
            data["args"] = args
        val template = cfg.getTemplate(file.name)
        val stringWriter = StringWriter()
        template.process(data, stringWriter)
        return stringWriter.buffer.toString()
    }


}
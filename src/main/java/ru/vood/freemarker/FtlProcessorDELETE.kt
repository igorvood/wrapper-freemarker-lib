package ru.vood.freemarker

import freemarker.template.Configuration

class FtlProcessorDELETE(val cfg: Configuration) : TemplateProcessor {

    constructor() : this(Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS))

    override fun processFile(file: String, vararg args: Any?): String {
/*
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
*/
        return ""
    }


}
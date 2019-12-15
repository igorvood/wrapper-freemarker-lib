package ru.vood.freemarker

import freemarker.cache.FileTemplateLoader
import freemarker.template.Configuration
import java.io.File
import java.io.StringWriter
import java.util.*

class FtlProcessorImpl : FtlProcessor {

    private val cfg = Configuration(Configuration.VERSION_2_3_29)

    override fun processFile(fileName: String, args: Array<Any?>?): String {
        val file = resolveFile(fileName)
        if (!file.exists())
            throw IllegalStateException("File $fileName doesn't exists")
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

    override fun processFile(clazz: Class<*>, fileName: String, args: Array<Any?>?) = processFile("""${clazz.name.replace(".", "/")}/$fileName""", args)

    private fun resolveFile(fileName: String): File {
        val resource = javaClass.classLoader.getResource(fileName)
        Optional.ofNullable(resource).orElseThrow { throw java.lang.IllegalStateException("File $fileName not found") }
        return File(resource.path)
    }
}
package ru.vood.freemarker

import freemarker.template.Configuration
import java.io.File
import java.sql.DriverManager

class FtlDataBaseProcessorImpl(
        val cfg: Configuration,
        val jdbcDriver: String,
        val url: String,
        private val user: String,
        private val password: String) : TemplateProcessor {

    override fun processFile(file: File, args: Array<Any?>?): String {
        val forName = Class.forName(jdbcDriver)
        val c = DriverManager.getConnection(url, user, password)
        return ""
    }
}
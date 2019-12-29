package ru.vood.freemarker

import java.io.File

interface FtlProcessor {

    fun processFile(fileName: String, args: Array<Any?>? = null): String

    fun processFile(file: File, args: Array<Any?>? = null): String

    fun processFile(clazz: Class<*>, fileName: String, args: Array<Any?>? = null): String

}
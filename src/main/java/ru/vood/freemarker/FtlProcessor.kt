package ru.vood.freemarker

interface FtlProcessor {

    fun processFile(fileName: String, args: Array<Any?>? = null): String

    fun processFile(clazz: Class<*>, fileName: String, args: Array<Any?>? = null): String

}
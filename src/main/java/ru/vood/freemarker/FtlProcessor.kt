package ru.vood.freemarker

interface FtlProcessor {

    fun processFile(fileName: String, args: Array<Any?>?): String

}
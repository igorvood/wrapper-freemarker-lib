package ru.vood.freemarker

import java.io.File
import java.util.*

interface FtlProcessor {

    //    fun processFile(fileName: String, args: Array<Any?>? = null): String
    fun processFile(file: File, args: Array<Any>? = null): String

    fun processFile(fileName: String, args: Array<Any>?) = processFile(resolveFile(fileName), args)

    fun processFile(clazz: Class<*>, fileName: String, args: Array<Any>?) = processFile("""${clazz.name.replace(".", "/")}/$fileName""", args)

    private fun resolveFile(fileName: String): File {
        val resource = javaClass.classLoader.getResource(fileName)
        Optional.ofNullable(resource).orElseThrow { throw java.lang.IllegalStateException("File $fileName not found") }
        return File(resource.path)
    }


//    fun processFile(clazz: Class<*>, fileName: String, args: Array<Any?>? = null): String

}
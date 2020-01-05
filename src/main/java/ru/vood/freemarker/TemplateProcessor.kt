package ru.vood.freemarker

import java.io.File

interface TemplateProcessor {

    //    fun processFile(fileName: String, args: Array<Any?>? = null): String
    fun processFile(file: File, args: Array<Any?>? = null): String

    fun processFile(fileName: String, args: Array<Any?>? = null) = processFile(resolveFile(fileName), args)

    fun processFile(clazz: Class<*>, fileName: String, args: Array<Any?>? = null) = processFile("""${clazz.name.replace(".", "/")}/$fileName""", args)

    private fun resolveFile(fileName: String): File {
        val resource = javaClass.classLoader.getResource(fileName)
        if (resource == null) {
            return File(fileName)
        }
//        Optional.ofNullable(resource).orElseThrow { throw SqlFtlException("File $fileName not found") }
        return File(resource.path)
    }


//    fun processFile(clazz: Class<*>, fileName: String, args: Array<Any?>? = null): String

}
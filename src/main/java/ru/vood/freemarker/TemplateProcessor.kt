package ru.vood.freemarker

interface TemplateProcessor {

    fun registerSharedVar(name: String, `val`: Any)

    fun processFile(fileName: String, vararg args: Any?): String

    fun processFile(clazz: Class<*>, fileName: String, vararg args: Any?) = processFile("""${clazz.name.replace(".", "/")}/$fileName""", args)

}
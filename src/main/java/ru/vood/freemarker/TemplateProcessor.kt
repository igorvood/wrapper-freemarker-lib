package ru.vood.freemarker

interface TemplateProcessor {

    //    fun processFile(fileName: String, args: Array<Any?>? = null): String
//    fun processFile(file: File, vararg args: Any? ): String

    fun processFile(fileName: String, vararg args: Any?): String

    fun processFile(clazz: Class<*>, fileName: String, vararg args: Any?) = processFile("""${clazz.name.replace(".", "/")}/$fileName""", args)

//    private fun resolveFile(fileName: String): File {
//        val resource = javaClass.classLoader.getResource(fileName)
//        if (resource == null) {
//            return File(fileName)
//        }
////        Optional.ofNullable(resource).orElseThrow { throw SqlFtlException("File $fileName not found") }
//        return File(resource.path)
//    }


//    fun processFile(clazz: Class<*>, fileName: String, args: Array<Any?>? = null): String

}
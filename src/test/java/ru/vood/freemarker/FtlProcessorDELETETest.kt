package ru.vood.freemarker

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.vood.freemarker.ext.sql.SqlFtlException
import java.util.*

internal class FtlProcessorDELETETest {

    private lateinit var ftlProcessor: TemplateProcessor

    @BeforeEach
    fun setUp() {
        ftlProcessor = FtlProcessorDELETE()
    }

    @Test
    fun processFileNoParam() {
        val processFile = ftlProcessor.processFile(fileName = "ru/vood/freemarker/FtlProcessorImplTest/FtlProcessorImplTestNoParam.ftl")
        Assertions.assertEquals("--NO PARAM--", processFile)
    }

    @Test
    fun processFileByClassNoParam() {
        val processFile = ftlProcessor.processFile(FtlProcessorDELETETest::class.java, "FtlProcessorImplTestNoParam.ftl")
        Assertions.assertEquals("--NO PARAM--", processFile)
    }

    @Test
    fun processFileByClassWithParam() {
        val stringParam = "zxc"
        val processFile = ftlProcessor.processFile(FtlProcessorDELETETest::class.java, "FtlProcessorImplTestWithParam.ftl", arrayOf(stringParam))
        Assertions.assertEquals("PARAM->$stringParam", processFile)
    }

    @Test
    fun processFileByClassWithParamObject_1() {
        val stringParam = Car("BMW", 1234, Date())
        val processFile = ftlProcessor.processFile(FtlProcessorDELETETest::class.java, "FtlProcessorImplTestWithParam.ftl", arrayOf(stringParam))
        Assertions.assertEquals("PARAM->$stringParam", processFile)
    }

    @Test
    fun processFileByClassWithParamObject_2() {
        val stringParam = Car("BMW", 123456789, Date(100, 3, 13))
        val processFile = ftlProcessor.processFile(FtlProcessorDELETETest::class.java, "FtlProcessorImplTestWithParamObject.ftl", arrayOf(stringParam))
        Assertions.assertTrue(processFile.contains("model->BMW,price->123 456 789,date->"))
        Assertions.assertTrue(processFile.contains(",dateTime->"))
        Assertions.assertEquals("model->BMW,price->123 456 789,date->13.04.2000,dateTime->13.04.2000 0:00:00", processFile)
    }

    @Test
    fun processNotExistsFile() {
        Assertions.assertThrows(SqlFtlException::class.java)
        { ftlProcessor.processFile("qwerty.ftl") }
    }
}
package ru.vood.freemarker

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.vood.freemarker.ext.sql.SqlFtlException
import java.util.*

internal class FtlProcessorTest {

    private lateinit var ftlProcessor: TemplateProcessor

    @BeforeEach
    fun setUp() {
        ftlProcessor = FtlProcessor()
    }

    @Test
    fun processFileNoParam() {
        val processFile = ftlProcessor.processFile(fileName = "/ru/vood/freemarker/FtlProcessorTest/FtlProcessorImplTestNoParam.ftl")
        Assertions.assertEquals("--NO PARAM--", processFile)
    }

    @Test
    fun processFileByClassNoParam() {
        val processFile = ftlProcessor.processFile(FtlProcessorTest::class.java, "FtlProcessorImplTestNoParam.ftl")
        Assertions.assertEquals("--NO PARAM--", processFile)
    }

    @Test
    fun processFileByClassWithParam() {
        val stringParam = "zxc"
        val processFile = ftlProcessor.processFile(FtlProcessorTest::class.java, "FtlProcessorImplTestWithParam.ftl", stringParam)
        Assertions.assertEquals("PARAM->$stringParam", processFile)
    }

    @Test
    fun processFileByClassWithParamObject_1() {
        val car = Car("BMW", 1234, Date())
        val car1 = Car("VW", 987, Date())
        val processFile = ftlProcessor.processFile(FtlProcessorTest::class.java, "FtlProcessorImplTestWithParam.ftl", car, car1)
        Assertions.assertEquals("PARAM->$car", processFile)
    }

    @Test
    fun processFileByClassWithParamObject_2() {
        val stringParam = Car("BMW", 123456789, Date(100, 3, 13))
        val processFile = ftlProcessor.processFile(FtlProcessorTest::class.java, "FtlProcessorImplTestWithParamObject.ftl", stringParam)
        Assertions.assertTrue(processFile.contains("model->BMW,price->123456789,date->"))
        Assertions.assertTrue(processFile.contains(",dateTime->"))
        Assertions.assertEquals("model->BMW,price->123456789,date->2000-04-13,dateTime->2000-04-13 00:00:00", processFile)
    }

    @Test
    fun processFileByClassWithParamObject_3() {
        val stringParam = Car("BMW", 123456789, Date(100, 3, 13))
        ftlProcessor.registerSharedVar("carJava", stringParam)
        val processFile = ftlProcessor.processFile(FtlProcessorTest::class.java, "FtlProcessorImplTestWithParamObject2.ftl")
        Assertions.assertTrue(processFile.contains("model->BMW,price->123456789,date->"))
        Assertions.assertTrue(processFile.contains(",dateTime->"))
        Assertions.assertEquals("model->BMW,price->123456789,date->2000-04-13,dateTime->2000-04-13 00:00:00", processFile)
    }

    @Test
    fun processNotExistsFile() {
        Assertions.assertThrows(SqlFtlException::class.java)
        { ftlProcessor.processFile("qwerty.ftl") }
    }
}
package ru.vood.freemarker

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FtlProcessorImplTest {

    private lateinit var ftlProcessor: FtlProcessor

    @BeforeEach
    fun setUp() {
        ftlProcessor = FtlProcessorImpl()
    }

    @Test
    fun processFileNoParam() {
        val processFile = ftlProcessor.processFile("ru/vood/freemarker/FtlProcessorImplTest/FtlProcessorImplTestNoParam.ftl")
        Assertions.assertEquals("--NO PARAM--", processFile)
    }

    @Test
    fun processFileByClassNoParam() {
        val processFile = ftlProcessor.processFile(FtlProcessorImplTest::class.java, "FtlProcessorImplTestNoParam.ftl")
        Assertions.assertEquals("--NO PARAM--", processFile)
    }

    @Test
    fun processFileByClassWithParam() {
        val stringParam = "zxc"
        val processFile = ftlProcessor.processFile(FtlProcessorImplTest::class.java, "FtlProcessorImplTestWithParam.ftl", arrayOf(stringParam))
        Assertions.assertEquals("PARAM->$stringParam", processFile)
    }

    @Test
    fun processNotExistsFile() {
        Assertions.assertThrows(IllegalStateException::class.java)
        { ftlProcessor.processFile("qwerty.ftl") }
    }
}
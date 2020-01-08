package sfqtl

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class DataBaseFtlProcessorTest : AbstractDataBaseFtlProcessorTests() {

    private fun testData(): Stream<Arguments?>? {
        return Stream.of(
                "test_template_name",
                "collection_from_query",
                "clob_from_query",
                "struct_from_query",
                "sql_types_query",
                "sql_types_call",
                "rs_transponse",
                "test_static"
        )
                .map { arguments: String -> Arguments.of(arguments) }
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun checkFtlFileProcessResult(fileName: String) {
        checkFtlFileProcessResult("$fileName.txt", "sfqtl/$fileName.ftl")
    }


}
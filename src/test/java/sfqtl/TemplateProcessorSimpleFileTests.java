package sfqtl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class TemplateProcessorSimpleFileTests extends AbstractTests {

    private static Stream<Arguments> testData() {
        return
                Stream.of(
                        "test_template_name",
                        "collection_from_query",
                        "clob_from_query",
                        "struct_from_query",
                        "sql_types_query",
                        "sql_types_call",
                        "rs_transponse",
                        "test_static"
                )
                        .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("testData")
    void checkFtlFileProcessResult(String fileName) {
        checkFtlFileProcessResult(fileName + ".txt", "sfqtl/" + fileName + ".ftl");
    }

}

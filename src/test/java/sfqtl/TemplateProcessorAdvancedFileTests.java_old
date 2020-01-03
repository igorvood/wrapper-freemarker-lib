package sfqtl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class TemplateProcessorAdvancedFileTests extends AbstractTests {

    private static Stream<Arguments> testData() {
        return
                Stream.of(
                        Arguments.of("test_passed_args.1.txt", "test_passed_args.ftl", new Object[]{}),
                        Arguments.of("test_passed_args.2.txt", "test_passed_args.ftl", new Object[]{"бяка", "", null, "бука"})
                );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void test(String expectedResultFileName, String ftlFileName, Object... args) {
        checkFtlFileProcessResult(expectedResultFileName, "sfqtl/" + ftlFileName, args);
    }

}

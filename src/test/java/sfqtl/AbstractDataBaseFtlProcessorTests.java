package sfqtl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.vood.freemarker.DataBaseFtlProcessor;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestConfig.class})
@ExtendWith(SpringExtension.class)
abstract class AbstractDataBaseFtlProcessorTests {

    @Autowired
    DataSource dataSource;

    @Autowired()
    @Qualifier("dbConnectionUrl")
    String dbConnectionUrl;

    @Autowired()
    @Qualifier("dbConnectionUser")
    String dbConnectionUser;

    @Autowired()
    @Qualifier("dbConnectionPassword")
    String dbConnectionPassword;

    DataBaseFtlProcessor dataBaseFtlProcessor;
    private DataSourceTransactionManager transactionManager;

    @BeforeAll
    private void setup() {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        this.dataBaseFtlProcessor = new DataBaseFtlProcessor("oracle.jdbc.driver.OracleDriver", dbConnectionUrl, dbConnectionUser, dbConnectionPassword);
        this.transactionManager = new DataSourceTransactionManager(dataSource);
    }

    void checkFtlFileProcessResult(String expectedResultFileName, String ftlFileName, Object... args) {
        String actualResult = process(ftlFileName, args);
        List<String> actualLines = Arrays.asList(actualResult.split("\\R"));
        List<String> expectedLines = loadListOfStrings(expectedResultFileName);
        Assertions.assertEquals(expectedLines, actualLines);
    }

    String process(String fileName, Object... args) {
        return dataBaseFtlProcessor.processFile(fileName, args);
    }

    private List<String> loadListOfStrings(String fileName) {
        try {
            URL fileURL = getClass().getResource(fileName);
            Assertions.assertNotNull(fileURL, () -> "file " + fileName + " was not found");
            return Files.readAllLines(Paths.get(fileURL.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw ExceptionUtils.throwAsUncheckedException(e);
        }
    }


}

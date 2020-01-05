package sfqtl;

import freemarker.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import ru.vood.freemarker.ext.processor.SpringFtlProcessor;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestConfig.class})
@ExtendWith(SpringExtension.class)
abstract class AbstractTests {

    @Autowired
    DataSource dataSource;
    SpringFtlProcessor sqlFtlProcessor;
    private DataSourceTransactionManager transactionManager;

    @BeforeAll
    private void setup() {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        this.sqlFtlProcessor = new SpringFtlProcessor(template);
        this.transactionManager = new DataSourceTransactionManager(dataSource);
    }

    void checkFtlFileProcessResult(String expectedResultFileName, String ftlFileName, Object... args) {
        String actualResult = process(ftlFileName, args);
        List<String> actualLines = Arrays.asList(actualResult.split("\\R"));
        List<String> expectedLines = loadListOfStrings(expectedResultFileName);
        Assertions.assertEquals(expectedLines, actualLines);
    }

    String process(Template template, Object... args) {
        StringWriter sw = new StringWriter();

        TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        return
                tt.execute(
                        transactionStatus -> {
                            sqlFtlProcessor.process(template, sw, args);
                            return sw.toString();
                        }
                );
    }

    private String process(String templateName, Object... args) {
        TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        Template template = sqlFtlProcessor.getTemplate(templateName);

        return process(template, args);
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

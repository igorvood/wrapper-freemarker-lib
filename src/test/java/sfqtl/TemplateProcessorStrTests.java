package sfqtl;

import freemarker.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vood.freemarker.ext.sql.SqlFtlException;

class TemplateProcessorStrTests extends AbstractTests {

    private static final String ERROR_TEMPLATE_NAME = "ошибочный шаблон";

    @Test
    void textPlainTextFtl() {

        final String txt = "значение переменной равно ";

        Template template =
                this.sqlFtlProcessor.getTemplateFromString(
                        "тестовый шаблон",
                        "<#assign x = 777/>\n" + txt + "${x?c}"
                );
        String result = this.process(template);
        Assertions.assertEquals(txt + "777", result);
    }

    @Test
    void textPlainTextFtlErrorProcessing() {

        String expectedErrorText = "ожидаемый текст";

        Template template =
                this.sqlFtlProcessor.getTemplateFromString(
                        ERROR_TEMPLATE_NAME, "<#stop '" + expectedErrorText + "'/>"
                );

        Assertions.assertTrue(getErrorFromBuggyFtl(template).contains(expectedErrorText));
    }

    @Test
    void textPlainTextFtlTemplateNameFromError() {
        Template template =
                this.sqlFtlProcessor.getTemplateFromString(
                        ERROR_TEMPLATE_NAME, "<#assign r = default_connection().query('select ошибка')/>"
                );
        Assertions.assertTrue(getErrorFromBuggyFtl(template).contains(ERROR_TEMPLATE_NAME));
    }

    @Test
    void textPlainTextFtlTemplateNameFromStdFunction() {
        final String testTemplateName = "хороший шаблон";
        Template template =
                this.sqlFtlProcessor.getTemplateFromString(testTemplateName, "${.current_template_name}");
        String res = process(template);
        Assertions.assertEquals(testTemplateName, res);
    }

    private String getErrorFromBuggyFtl(Template template) {
        try {
            this.process(template);
            Assertions.fail("что-то не упало, а должно было");
        } catch (SqlFtlException ex) {
            Assertions.assertNotNull(ex.getCause());
            String message = ex.getCause().getMessage();
            Assertions.assertNotNull(message);
            return message;
        }
        throw new IllegalStateException("кошмар какой-то");
    }


}

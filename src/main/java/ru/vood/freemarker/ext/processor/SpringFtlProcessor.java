package ru.vood.freemarker.ext.processor;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;
import ru.vood.freemarker.ext.sql.ConnectionAdapter;

import java.util.List;

public class SpringFtlProcessor extends SimpleFtlProcessor {
    private final ConnectionAdapter defaultConnection;

    public SpringFtlProcessor(JdbcOperations jdbcOperations) {
        super();
        this.defaultConnection = new ConnectionAdapter(this, jdbcOperations);
        registerSharedVar("default_connection", getGetDefaultConnectionMethod());
    }

    public TemplateMethodModelEx getGetDefaultConnectionMethod() {
        return
                new TemplateMethodModelEx() {
                    @Override
                    public Object exec(List args) throws TemplateModelException {
                        Assert.isTrue(
                                args.isEmpty(),
                                () -> "Wrong number of arguments: expected 0, got " + args.size()
                        );
                        return defaultConnection;
                    }
                };
    }

}
package ru.vood.freemarker.ext.processor;

import freemarker.template.TemplateMethodModelEx;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;
import ru.vood.freemarker.ext.sql.ConnectionAdapter;

public class SpringFtlProcessor extends SimpleFtlProcessor {
    private final ConnectionAdapter defaultConnection;

    public SpringFtlProcessor(JdbcOperations jdbcOperations) {
        super();
        this.defaultConnection = new ConnectionAdapter(this, jdbcOperations);
        setSharedVariable("default_connection", getGetDefaultConnectionMethod());
    }

    private TemplateMethodModelEx getGetDefaultConnectionMethod() {
        return
                args -> {
                    Assert.isTrue(
                            args.isEmpty(),
                            () -> "Wrong number of arguments: expected 0, got " + args.size()
                    );
                    return defaultConnection;
                };
    }

}
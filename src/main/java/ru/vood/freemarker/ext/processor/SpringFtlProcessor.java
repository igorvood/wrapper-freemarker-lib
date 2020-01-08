package ru.vood.freemarker.ext.processor;

import freemarker.template.Template;
import freemarker.template.TemplateMethodModelEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import ru.vood.freemarker.ext.sql.ConnectionAdapter;

import javax.sql.DataSource;
import java.io.Writer;

public class SpringFtlProcessor extends SimpleFtlProcessor {
    private final ConnectionAdapter defaultConnection;

    public SpringFtlProcessor(JdbcTemplate jdbcOperations) {
        super();
        this.defaultConnection = new ConnectionAdapter(this, jdbcOperations);
        registerSharedVar("default_connection", getGetDefaultConnectionMethod());
    }

    public TemplateMethodModelEx getGetDefaultConnectionMethod() {
        return
                args -> {
                    Assert.isTrue(
                            args.isEmpty(),
                            () -> "Wrong number of arguments: expected 0, got " + args.size()
                    );
                    return defaultConnection;
                };
    }
/*

    public ConnectionAdapter getDefaultConnection() {
        return defaultConnection;
    }
*/

    @NotNull
    @Override
    public String process(@NotNull Template template, @Nullable Object... args) {
        final DataSource dataSource = defaultConnection.getJdbcOperations().getDataSource();
        TransactionTemplate tt = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return
                tt.execute(
                        transactionStatus -> super.process(template, args)
                );
    }

    @Override
    public void process(@NotNull Template template, @NotNull Writer dest, @Nullable Object... args) {
        final DataSource dataSource = defaultConnection.getJdbcOperations().getDataSource();
        TransactionTemplate tt = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(
                transactionStatus -> {
                    SpringFtlProcessor.super.process(template, dest, args);
                    return dest;
                }
        );
    }
}
package ru.vood.freemarker.ext.processor;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateMethodModelEx;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;
import ru.vood.freemarker.ext.sql.ConnectionAdapter;
import ru.vood.freemarker.ext.sql.FtlDefaultObjectWrapper;
import ru.vood.freemarker.ext.sql.SharedHash;

public class SpringFtlProcessor extends AbstractFtlProcessor {
    private final ConnectionAdapter defaultConnection;

    public SpringFtlProcessor(JdbcOperations jdbcOperations) {
        super();
        this.defaultConnection = new ConnectionAdapter(this, jdbcOperations);
        // Set default settings
        setObjectWrapper(new FtlDefaultObjectWrapper(this.getIncompatibleImprovements()));
        setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        setLocalizedLookup(false);
        this.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));
        this.setLocalizedLookup(false);
        setDefaultEncoding("UTF8");
        setNumberFormat("computer");
        setDateFormat("yyyy-MM-dd");
        setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        setCacheStorage(new NullCacheStorage());
        // Register user-defined variables and methods
        registerSharedVar("shared_hash", new SharedHash());
        registerSharedVar("static", getGetStaticMethod());
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
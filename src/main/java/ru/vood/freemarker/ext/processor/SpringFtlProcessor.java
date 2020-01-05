package ru.vood.freemarker.ext.processor;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.*;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;
import ru.vood.freemarker.ext.sql.ConnectionAdapter;
import ru.vood.freemarker.ext.sql.FtlDefaultObjectWrapper;
import ru.vood.freemarker.ext.sql.SharedHash;
import ru.vood.freemarker.ext.sql.SqlFtlException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

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

    public String process(Template template, Object... args) {
        StringWriter sw = new StringWriter();
        process(template, sw, args);
        return sw.toString();
    }

    public void process(Template template, Writer dest, Object... args) {
        SimpleHash root = new SimpleHash(getFtlDefaultObjectWrapper());
        registerSharedVar("template_args", args);
        try {
            template.process(root, dest);
        } catch (IOException | TemplateException e) {
            throw new SqlFtlException("ftl processing exception", e);
        }
    }

    public void registerSharedVar(String name, Object val) {
        try {
            setSharedVariable(name, val);
        } catch (TemplateModelException e) {
            throw new SqlFtlException("Unable to register " + name + " variable", e);
        }
    }

    public FtlDefaultObjectWrapper getFtlDefaultObjectWrapper() {
        return (FtlDefaultObjectWrapper) this.getObjectWrapper();
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
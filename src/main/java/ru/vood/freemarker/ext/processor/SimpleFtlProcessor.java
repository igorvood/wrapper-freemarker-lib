package ru.vood.freemarker.ext.processor;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.TemplateExceptionHandler;
import ru.vood.freemarker.ext.sql.FtlDefaultObjectWrapper;
import ru.vood.freemarker.ext.sql.SharedHash;

public class SimpleFtlProcessor extends AbstractFtlProcessor {

    public SimpleFtlProcessor() {
        super();
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
    }

}
package ru.vood.freemarker.ext.sql.model;

import freemarker.core.CollectionAndSequence;
import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;
import org.springframework.util.Assert;
import ru.vood.freemarker.ext.sql.FetchedResultSet;
import ru.vood.freemarker.ext.sql.FetchedResultSetTransposed;

import java.util.ArrayList;
import java.util.Set;

public class FetchedResultSetModel extends BeanModel implements TemplateSequenceModel, TemplateScalarModel {
    private static final String TRANSPOSE_METHOD_NAME = "transpose";
    private final ObjectWrapper objectWrapper;
    private final FetchedResultSet frs;

    public FetchedResultSetModel(FetchedResultSet frs, BeansWrapper objectWrapper) {
        super(frs, objectWrapper);
        this.objectWrapper = objectWrapper;
        this.frs = frs;
    }

    public TemplateModel get(int index) {
        return new FetchedResultSetRowModel(frs, index, objectWrapper);
    }

    public int size() {
        return frs.getRows().size();
    }

    public TemplateModel get(String key) throws TemplateModelException {
        if (key.equals(TRANSPOSE_METHOD_NAME)) {
            return transpose();
        }
        return super.get(key);
    }

    private TemplateMethodModelEx transpose() {
        return args -> {
            Assert.isTrue(args.isEmpty(), () -> "No arguments needed, but args=" + args);
            return wrap(new FetchedResultSetTransposed(frs));
        };
    }

    public TemplateCollectionModel keys() {
        Set<String> keySetEx = super.keySet();
        keySetEx.add(TRANSPOSE_METHOD_NAME);
        return new CollectionAndSequence(new SimpleSequence(keySetEx, objectWrapper));
    }

    public TemplateCollectionModel values() {
        return new SimpleCollection(new ArrayList(0), objectWrapper);
    }

    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        frs.getColumnLabels().forEach(cl -> sb.append(cl).append('\t'));
        sb.append('\n');
        frs.getRows().forEach(row -> {
            row.forEach(columnVal -> sb.append(columnVal).append('\t'));
            sb.append('\n');
        });
        return sb.toString();
    }
}
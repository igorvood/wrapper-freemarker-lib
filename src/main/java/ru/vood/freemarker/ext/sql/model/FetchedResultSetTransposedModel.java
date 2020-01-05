package ru.vood.freemarker.ext.sql.model;

import freemarker.template.*;
import ru.vood.freemarker.ext.sql.FetchedResultSetTransposed;

public class FetchedResultSetTransposedModel extends WrappingTemplateModel
        implements TemplateSequenceModel, TemplateHashModelEx {
    private final FetchedResultSetTransposed frst;

    public FetchedResultSetTransposedModel(FetchedResultSetTransposed frst, ObjectWrapper wrapper) {
        super(wrapper);
        this.frst = frst;
    }

    public TemplateModel get(int columnIndex) throws TemplateModelException {
        return wrap(frst.getTransposedData().get(columnIndex));
    }

    public TemplateModel get(String key) throws TemplateModelException {
        return wrap(frst.getTransposedData().get(frst.getResultSet().getColumnIndex(key)));
    }

    public int size() {
        return frst.getTransposedData().size();
    }

    public TemplateCollectionModel keys() {
        return new SimpleCollection(frst.getResultSet().getColumnLabels(), getObjectWrapper());
    }

    public TemplateCollectionModel values() {
        return new SimpleCollection(frst.getTransposedData(), getObjectWrapper());
    }

    public boolean isEmpty() {
        return frst.getTransposedData().isEmpty();
    }
}
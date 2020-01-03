package ru.vood.freemarker.ext.sql.model;


import freemarker.template.*;
import ru.vood.freemarker.ext.sql.FetchedResultSet;


/**
 * This class wraps {@link java.sql.ResultSet}'s row and adapts it for using in FTL both as a sequence and a hash of
 * rows.
 */
public class FetchedResultSetRowModel extends WrappingTemplateModel implements TemplateSequenceModel, TemplateHashModelEx {

    private final FetchedResultSet resultSet;
    private final int rowIndex;

    FetchedResultSetRowModel(FetchedResultSet frs, int index, ObjectWrapper wrapper) {
        super(wrapper);
        this.resultSet = frs;
        this.rowIndex = index;
    }

    public TemplateModel get(int index) throws TemplateModelException {
        return wrap(resultSet.getRows().get(rowIndex).get(index));
    }

    public TemplateModel get(String key) throws TemplateModelException {
        return wrap(resultSet.getRows().get(rowIndex).get(resultSet.getColumnIndex(key)));
    }

    public int size() {
        return resultSet.getColumnLabels().size();
    }

    public TemplateCollectionModel keys() {
        return new SimpleCollection(resultSet.getColumnLabels(), getObjectWrapper());
    }

    public TemplateCollectionModel values() {
        return new SimpleCollection(resultSet.getRows().get(rowIndex), getObjectWrapper());
    }

    public boolean isEmpty() {
        return false;
    }

}
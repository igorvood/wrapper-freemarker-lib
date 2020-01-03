package ru.vood.freemarker.ext.sql;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import ru.vood.freemarker.ext.sql.model.*;

import java.sql.Array;
import java.sql.Clob;
import java.sql.Struct;

public class FtlDefaultObjectWrapper extends freemarker.template.DefaultObjectWrapper {


    public FtlDefaultObjectWrapper(Version incompatibleImprovements) {
        super(incompatibleImprovements);
    }


    protected TemplateModel handleUnknownType(Object obj) throws TemplateModelException {
        if (obj instanceof Array) {
            return new ArrayModel((Array) obj, this);
        }
        if (obj instanceof Clob) {
            return new ClobModel((Clob) obj, this);
        }
        if (obj instanceof Struct) {
            return new StructModel((Struct) obj, this);
        }
        if (obj instanceof FetchedResultSet) {
            return new FetchedResultSetModel((FetchedResultSet) obj, this);
        }
        if (obj instanceof FetchedResultSetTransposed) {
            return new FetchedResultSetTransposedModel((FetchedResultSetTransposed) obj, this);
        }
        return super.handleUnknownType(obj);
    }


}

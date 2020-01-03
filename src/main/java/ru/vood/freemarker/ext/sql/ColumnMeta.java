package ru.vood.freemarker.ext.sql;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColumnMeta {

    private final boolean autoIncrement;
    private final int nullable;
    private final boolean signed;
    private final String columnLabel;
    private final String columnName;
    private final int precision;
    private final int scale;
    private final int columnType;
    private final String columnTypeName;
    private final boolean readOnly;
    private final boolean writable;
    private final boolean definitelyWritable;
    private final String columnClassName;

    private ColumnMeta(boolean autoIncrement, int nullable, boolean signed, String columnLabel, String columnName, int precision, int scale, int columnType, String columnTypeName, boolean readOnly, boolean writable, boolean definitelyWritable, String columnClassName) {
        this.autoIncrement = autoIncrement;
        this.nullable = nullable;
        this.signed = signed;
        this.columnLabel = columnLabel;
        this.columnName = columnName;
        this.precision = precision;
        this.scale = scale;
        this.columnType = columnType;
        this.columnTypeName = columnTypeName;
        this.readOnly = readOnly;
        this.writable = writable;
        this.definitelyWritable = definitelyWritable;
        this.columnClassName = columnClassName;
    }

    static List<ColumnMeta> of(ResultSetMetaData m) throws SQLException {
        int colsCount = m.getColumnCount();
        List<ColumnMeta> res = new ArrayList<>(colsCount);
        for (int i = 1; i <= colsCount; i++) {
            res.add(new ColumnMeta(
                    m.isAutoIncrement(i), m.isNullable(i), m.isSigned(i), m.getColumnLabel(i), m.getColumnName(i),
                    m.getPrecision(i), m.getScale(i), m.getColumnType(i), m.getColumnTypeName(i),
                    m.isReadOnly(i), m.isWritable(i), m.isDefinitelyWritable(i), m.getColumnClassName(i)));
        }
        return res;
    }


    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public int getNullable() {
        return nullable;
    }

    public boolean isSigned() {
        return signed;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    public int getColumnType() {
        return columnType;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isWritable() {
        return writable;
    }

    public boolean isDefinitelyWritable() {
        return definitelyWritable;
    }

    public String getColumnClassName() {
        return columnClassName;
    }
}

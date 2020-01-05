package ru.vood.freemarker.ext.sql;

public class SqlFtlException extends RuntimeException {
    public SqlFtlException(String message) {
        super(message);
    }

    public SqlFtlException(String message, Throwable cause) {
        super(message, cause);
    }
}

package ru.vood.freemarker.ext.sql;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.vood.freemarker.ext.processor.ProcessFtl;

import java.util.List;
import java.util.Map;


/**
 * This class is a wrapper for working with JDBC connections in FTL. Some basic methods are accessible directly, the
 * others can be invoked via the inner JDBC connection instance.
 */
public class ConnectionAdapter {


    private final ProcessFtl config;
    private final JdbcTemplate jdbcOperations;
    private final QueryExecutor qe;
    private final CallExecutor ce;

    public ConnectionAdapter(ProcessFtl config, JdbcTemplate jdbcOperations) {
        this.config = config;
        this.jdbcOperations = jdbcOperations;
        qe = new QueryExecutor(this);
        ce = new CallExecutor(this);
    }

    public JdbcTemplate getJdbcOperations() {
        return jdbcOperations;
    }

    ProcessFtl getConfig() {
        return config;
    }

    public FetchedResultSet query(String sql) {
        return qe.executeQuery(sql);
    }

    public FetchedResultSet query(String sql, List binds) {
        return qe.executeQuery(sql, binds);
    }

    public Map call(String statement, Map inBinds, Map outBinds) {
        return ce.executeCall(statement, inBinds, outBinds);
    }
}

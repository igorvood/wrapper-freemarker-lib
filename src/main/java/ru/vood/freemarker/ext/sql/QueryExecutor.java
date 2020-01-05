package ru.vood.freemarker.ext.sql;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;


/**
 * This class is a database query executor. The result of each query is fully fetched and placed in memory, so be
 * careful while working with very large queries. Columns are got with {@link ResultSet#getObject(int)} and saved as is,
 * preserving their types.
 */
public class QueryExecutor {

    private final ConnectionAdapter connectionAdapter;

    QueryExecutor(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    public FetchedResultSet executeQuery(String query) {
        return executeQuery(query, null);
    }

    public FetchedResultSet executeQuery(String query, List binds) {
        if (query == null || "".equals(query.trim())) {
            throw new SqlFtlException("Unable to execute empty query");
        }
        return
                connectionAdapter.getJdbcOperations().query(
                        connection -> {
                            PreparedStatement ps = connection.prepareStatement(query);
                            if (binds != null && !binds.isEmpty()) {
                                int index = 1;
                                for (Object o : binds) {
                                    // JDBC can't work with java.util.Date directly
                                    if (o instanceof Date) o = TypeHelper.toSQLDate((Date) o);
                                    ps.setObject(index++, o);
                                }
                            }
                            return ps;
                        },
                        (ResultSet rs) -> new FetchedResultSet(rs, connectionAdapter.getConfig().getFtlDefaultObjectWrapper())
                );
    }

}

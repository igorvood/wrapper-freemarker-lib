/*
 * Copyright 2014-2016 Victor Osolovskiy, Sergey Navrotskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.vood.freemarker.ext.sql;


import org.springframework.jdbc.core.CallableStatementCallback;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is a database callable statement executor.
 */
public class CallExecutor {
    private final ConnectionAdapter connectionAdapter;


    CallExecutor(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    private static void setInBinds(CallableStatement cs, Map inBinds) throws SQLException {
        for (Object o1 : inBinds.entrySet()) {
            Map.Entry e = (Map.Entry) o1;
            int index;
            try {
                index = Integer.parseInt(e.getKey().toString());
            } catch (NumberFormatException ex) {
                throw new SqlFtlException("Wrong in bind variable index: expected int, got String", ex);
            }
            Object o = e.getValue();
            // JDBC can't work with java.util.Date directly
            if (o instanceof Date) o = TypeHelper.toSQLDate((Date) o);
            cs.setObject(index, o);
        }
    }

    private static void setOutBinds(CallableStatement cs, Map outBinds) throws SQLException {
        for (Object o1 : outBinds.entrySet()) {
            Map.Entry e = (Map.Entry) o1;
            int index;
            try {
                index = Integer.parseInt(e.getKey().toString());
            } catch (NumberFormatException ex) {
                throw new SQLException("Wrong out bind variable index: expected int, got String", ex);
            }
            Object o = e.getValue();
            String typeName;
            if (o instanceof String) {
                typeName = (String) o;
            } else {
                throw new SQLException("Unable to register type of out bind variable #" + index + ": " + o);
            }
            String sqlTypeName;
            String usrTypeName;
            int delimIndex = typeName.indexOf(':');
            if (delimIndex == -1) {
                sqlTypeName = typeName;
                usrTypeName = null;
            } else {
                sqlTypeName = typeName.substring(0, delimIndex);
                usrTypeName = typeName.substring(delimIndex + 1);
            }
            Integer sqlType;
            try {
                sqlType = TypeHelper.getIntValue(sqlTypeName);
            } catch (Exception ex) {
                throw new SQLException("Unknown SQL type of out bind variable #" + index + ": " + sqlTypeName, ex);
            }
            if (usrTypeName == null || "".equals(usrTypeName.trim())) {
                cs.registerOutParameter(index, sqlType);
            } else {
                cs.registerOutParameter(index, sqlType, usrTypeName);
            }
        }
    }

    public Map executeCall(String call, Map inBinds, Map outBinds) {
        if (call == null || "".equals(call.trim())) {
            throw new SqlFtlException("Unable to execute empty call");
        }
        if (inBinds == null) inBinds = Collections.emptyMap();
        if (outBinds == null) outBinds = Collections.emptyMap();
        return executeCallInternal(call, inBinds, outBinds);
    }

    private Map<String, Object> executeCallInternal(String call, Map inBinds, Map outBinds) {
        return connectionAdapter.getJdbcOperations().execute(
                connection -> {
                    CallableStatement cs = connection.prepareCall(call);
                    setInBinds(cs, inBinds);
                    setOutBinds(cs, outBinds);
                    return cs;
                },
                (CallableStatementCallback<Map<String, Object>>) cs -> {
                    cs.execute();
                    Map<String, Object> ret = new HashMap<>(outBinds.size(), 1);
                    for (Object o : outBinds.entrySet()) {
                        Map.Entry e = (Map.Entry) o;
                        int index = Integer.parseInt(e.getKey().toString());
                        Object retBind = cs.getObject(index);
                        if (retBind instanceof ResultSet) {
                            retBind = new FetchedResultSet((ResultSet) retBind, this.connectionAdapter.getConfig().getFtlDefaultObjectWrapper());
                        }
                        ret.put(String.valueOf(index), retBind);
                    }
                    return ret;
                });
    }
}

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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class contains the static result of fetching from a {@link ResultSet} and its metadata. This is needed because
 * the original result set can be fetched only once, so wrapping it directly may lead to getting empty result set in
 * FTL if the object is accessed twice.
 */
public class FetchedResultSet {

    private final List<ColumnMeta> meta;
    private final Map<String, Integer> columnIndices;
    private final List<List<Object>> rows;

    FetchedResultSet(ResultSet rs, FtlDefaultObjectWrapper ftlDefaultObjectWrapper) throws SQLException {
        try {
            this.meta = Collections.unmodifiableList(ColumnMeta.of(rs.getMetaData()));
            final int columnCount = meta.size();
            Map<String, Integer> tempColumnIndices = new HashMap<>(columnCount, 1);
            for (int i = 0; i < columnCount; i++) {
                String columnLabel = meta.get(i).getColumnLabel();
                // if two columns have same labels, save only the 1st one
                tempColumnIndices.putIfAbsent(columnLabel, i);
            }
            List<List<Object>> tempRows = new ArrayList<>(64);
            while (rs.next()) {
                List<Object> row = new ArrayList<>(columnCount);
                for (int i = 0; i < columnCount; i++) {
                    Object o = rs.getObject(i + 1);
                    if (o instanceof ResultSet) {
                        o = new FetchedResultSet((ResultSet) o, ftlDefaultObjectWrapper);
                    }
                    row.add(o);
                }
                tempRows.add(Collections.unmodifiableList(row));
            }
            this.rows = Collections.unmodifiableList(tempRows);
            this.columnIndices = Collections.unmodifiableMap(tempColumnIndices);
        } finally {
            if (!rs.isClosed()) {
                rs.close();
            }
        }
    }

    public List<String> getColumnLabels() {
        return meta.stream().map(ColumnMeta::getColumnLabel).collect(Collectors.toList());
    }

    public Integer getColumnIndex(String label) {
        return columnIndices.get(label);
    }

    public Integer findColumn(String label) {
        Integer index = getColumnIndex(label);
        if (index == null) return null;
        return index + 1;
    }

    public List<ColumnMeta> getMetaData() {
        return meta;
    }

    public Map<String, Integer> getColumnIndices() {
        return columnIndices;
    }

    public List<List<Object>> getRows() {
        return rows;
    }
}

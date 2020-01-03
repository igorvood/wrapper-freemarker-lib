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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class is a transposed {@link FetchedResultSet}.
 */
public class FetchedResultSetTransposed {

    private final FetchedResultSet resultSet;
    private final List<List<Object>> transposedData;

    public FetchedResultSetTransposed(FetchedResultSet frs) {
        this.resultSet = frs;
        int columnsCount = frs.getMetaData().size();
        List<List<Object>> tempTransposedData = new ArrayList<>();
        this.transposedData = Collections.unmodifiableList(tempTransposedData);
        List<List<Object>> origRows = frs.getRows();
        IntStream.range(0, columnsCount).forEach(
                columnIndex -> {
                    List<Object> columnData = new ArrayList<>(origRows.size());
                    origRows.forEach(row -> columnData.add(row.get(columnIndex)));
                    tempTransposedData.add(Collections.unmodifiableList(columnData));
                });
    }

    public List<List<Object>> getTransposedData() {
        return transposedData;
    }

    public FetchedResultSet getResultSet() {
        return resultSet;
    }


}

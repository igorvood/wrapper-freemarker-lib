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


/**
 * This class is a transposed {@link FetchedResultSet}.
 */
public class FetchedResultSetTransposed {


    public final FetchedResultSet resultSet;
    public final Object[][] transposedData;


    public FetchedResultSetTransposed(FetchedResultSet frs) {

        this.resultSet = frs;

        transposedData = new Object[resultSet.columnLabels.length][];
        for (int ri = 0; ri < resultSet.data.length; ri++) {
            for (int ci = 0; ci < resultSet.columnLabels.length; ci++) {
                if (transposedData[ci] == null) transposedData[ci] = new Object[resultSet.data.length];
                transposedData[ci][ri] = resultSet.data[ri][ci];
            }
        }
    }


}
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
package ru.vood.freemarker.ext.sql.model;


import freemarker.template.*;
import ru.vood.freemarker.ext.sql.FetchedResultSetTransposed;


/**
 * This class wraps {@link FetchedResultSetTransposed} and adapts it for using in FTL both as a sequence and a hash of
 * columns.
 */
public class FetchedResultSetTransposedModel extends WrappingTemplateModel
        implements TemplateSequenceModel, TemplateHashModelEx {
    private final FetchedResultSetTransposed frst;

    public FetchedResultSetTransposedModel(FetchedResultSetTransposed frst, ObjectWrapper wrapper) {
        super(wrapper);
        this.frst = frst;
    }

    public TemplateModel get(int columnIndex) throws TemplateModelException {
        return wrap(frst.getTransposedData().get(columnIndex));
    }

    public TemplateModel get(String key) throws TemplateModelException {
        return wrap(frst.getTransposedData().get(frst.getResultSet().getColumnIndex(key)));
    }

    public int size() {
        return frst.getTransposedData().size();
    }

    public TemplateCollectionModel keys() {
        return new SimpleCollection(frst.getResultSet().getColumnLabels(), getObjectWrapper());
    }

    public TemplateCollectionModel values() {
        return new SimpleCollection(frst.getTransposedData(), getObjectWrapper());
    }

    public boolean isEmpty() {
        return frst.getTransposedData().isEmpty();
    }


}
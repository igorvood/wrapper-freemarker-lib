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
package ru.vood.freemarker.ext.sql.model

import freemarker.template.*
import ru.vood.freemarker.ext.sql.FetchedResultSetTransposed

/**
 * This class wraps [FetchedResultSetTransposed] and adapts it for using in FTL both as a sequence and a hash of
 * columns.
 */
class FetchedResultSetTransposedModel(
        private val frst: FetchedResultSetTransposed,
        wrapper: ObjectWrapper?) : WrappingTemplateModel(wrapper), TemplateSequenceModel, TemplateHashModelEx {

    override fun get(columnIndex: Int): TemplateModel {
        return wrap(frst.transposedData[columnIndex])
    }

    override fun get(key: String): TemplateModel {
        return wrap(frst.transposedData[frst.resultSet.getColumnIndex(key)])
    }

    override fun size(): Int {
        return frst.transposedData.size
    }

    override fun keys(): TemplateCollectionModel {
        return SimpleCollection(frst.resultSet.columnLabels, objectWrapper)
    }

    override fun values(): TemplateCollectionModel {
        return SimpleCollection(frst.transposedData, objectWrapper)
    }

    override fun isEmpty(): Boolean {
        return frst.transposedData.isEmpty()
    }

}
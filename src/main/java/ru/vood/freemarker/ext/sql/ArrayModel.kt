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
package ru.vood.freemarker.ext.sql

import freemarker.ext.beans.BeanModel
import freemarker.ext.beans.BeansWrapper
import freemarker.template.*
import java.sql.SQLException
import java.util.*

/**
 * This class wraps [Array] and adapts it for using in FTL both as a sequence and as a bean.
 */
class ArrayModel(sqlArray: java.sql.Array, wrapper: BeansWrapper) : BeanModel(sqlArray, wrapper), TemplateSequenceModel {
    private val array: Array<Any>
    /**
     * Retrieves the i-th element in this sequence.
     *
     * @return the item at the specified index
     */
    override fun get(index: Int): TemplateModel {
        return wrap(array[index])
    }

    /**
     * Returns the array size.
     *
     * @return the number of items in the list
     */
    override fun size(): Int {
        return array.size
    }

    /**
     * Returns the empty list. Iteration through the `super.values()` list causes an exception.
     *
     * @return the empty list
     */
    override fun values(): TemplateCollectionModel {
        return SimpleCollection(ArrayList<Any>(0), wrapper)
    }

    init {
        try {
            array = sqlArray.array as Array<Any>
        } catch (e: SQLException) {
            throw TemplateModelException(e)
        }
    }
}
package ru.vood.freemarker.ext.sql.model

import freemarker.ext.beans.BeanModel
import freemarker.ext.beans.BeansWrapper
import freemarker.template.SimpleCollection
import freemarker.template.TemplateCollectionModel
import freemarker.template.TemplateModel
import freemarker.template.TemplateSequenceModel
import java.util.*

/**
 * This class wraps [Array] and adapts it for using in FTL both as a sequence and as a bean.
 */
class ArrayModel(sqlArray: java.sql.Array, wrapper: BeansWrapper) : BeanModel(sqlArray, wrapper), TemplateSequenceModel {
    private val array: Array<Any>

    init {
        array = sqlArray.array as Array<Any>
    }


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

}
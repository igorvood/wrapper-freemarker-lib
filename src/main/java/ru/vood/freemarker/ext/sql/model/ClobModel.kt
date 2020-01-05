package ru.vood.freemarker.ext.sql.model

import freemarker.ext.beans.BeanModel
import freemarker.ext.beans.BeansWrapper
import freemarker.template.SimpleCollection
import freemarker.template.TemplateCollectionModel
import freemarker.template.TemplateScalarModel
import java.sql.Clob
import java.util.*

/**
 * This class wraps [Clob] and adapts it for using in FTL as a string.
 */
class ClobModel(clob: Clob, wrapper: BeansWrapper) : BeanModel(clob, wrapper), TemplateScalarModel {
    private var string: String = clob.getSubString(1, clob.length().toInt())

    /**
     * Returns the string representation of this clob.
     *
     * @return clob as a string
     */
    override fun getAsString(): String {
        return string
    }

    /**
     * Returns the clob size.
     *
     * @return the number of characters
     */
    override fun size(): Int {
        return string.length
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
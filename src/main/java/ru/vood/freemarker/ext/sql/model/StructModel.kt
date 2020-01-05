package ru.vood.freemarker.ext.sql.model

import freemarker.ext.beans.BeanModel
import freemarker.ext.beans.BeansWrapper
import freemarker.template.SimpleCollection
import freemarker.template.TemplateCollectionModel
import freemarker.template.TemplateModel
import freemarker.template.TemplateSequenceModel
import java.sql.Struct
import java.util.*

/**
 * This class wraps [Struct] and adapts it for using in FTL both as a sequence and as a bean.
 */
class StructModel(sqlStruct: Struct,
                  wrapper: BeansWrapper) : BeanModel(sqlStruct, wrapper), TemplateSequenceModel {
    private val struct: Array<Any> = sqlStruct.attributes

    /**
     * Retrieves the i-th element in this structure.
     *
     * @return the item at the specified index
     */
    override fun get(index: Int): TemplateModel {
        return wrap(struct[index])
    }

    /**
     * Returns the structure size.
     *
     * @return the number of items in the structure
     */
    override fun size(): Int {
        return struct.size
    }

    /**
     * Returns the empty list. Iteration through the `super.values()` list causes an exception.
     *
     * @return the empty list
     */
    override fun values(): TemplateCollectionModel {
        return SimpleCollection(ArrayList<Any?>(0), wrapper)
    }

}
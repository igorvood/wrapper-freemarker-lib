package ru.vood.freemarker.ext.sql.model

import freemarker.template.*
import ru.vood.freemarker.ext.sql.FetchedResultSet

/**
 * This class wraps [java.sql.ResultSet]'s row and adapts it for using in FTL both as a sequence and a hash of
 * rows.
 */
class FetchedResultSetRowModel internal constructor(
        private val resultSet: FetchedResultSet,
        private val rowIndex: Int, wrapper: ObjectWrapper) : WrappingTemplateModel(wrapper), TemplateSequenceModel, TemplateHashModelEx {

    override fun get(index: Int): TemplateModel {
        return wrap(resultSet.rows[rowIndex][index])
    }

    override fun get(key: String): TemplateModel {
        return wrap(resultSet.rows[rowIndex][resultSet.getColumnIndex(key)])
    }

    override fun size(): Int {
        return resultSet.columnLabels.size
    }

    override fun keys(): TemplateCollectionModel {
        return SimpleCollection(resultSet.columnLabels, objectWrapper)
    }

    override fun values(): TemplateCollectionModel {
        return SimpleCollection(resultSet.rows[rowIndex], objectWrapper)
    }

    override fun isEmpty(): Boolean {
        return false
    }

}
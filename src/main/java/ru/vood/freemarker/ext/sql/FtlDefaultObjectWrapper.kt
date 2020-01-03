package ru.vood.freemarker.ext.sql

import freemarker.template.DefaultObjectWrapper
import freemarker.template.TemplateModel
import freemarker.template.Version
import ru.vood.freemarker.ext.sql.model.*
import java.sql.Array
import java.sql.Clob
import java.sql.Struct

/**
 * The FTLDB's default object wrapper. Extends FreeMarker's wrapper with SQL type wrapping.
 *
 *
 * Registered wrappers are:
 *
 *  * [ArrayModel] - treats SQL collections ([Array]) as sequences
 *  * [ClobModel] - treats CLOBs ([Clob]) as strings
 *  * [StructModel] - treats UDTs ([Struct]) as sequences of elements
 *  * [FetchedResultSetModel] - treats fetched result sets ([FetchedResultSet]) as 2-layer objects
 *  * [FetchedResultSetTransposedModel] - wraps transposed result sets
 *
 */
class FtlDefaultObjectWrapper(incompatibleImprovements: Version?) : DefaultObjectWrapper(incompatibleImprovements) {

    override fun handleUnknownType(obj: Any): TemplateModel {
        return when (obj) {
            is Array -> ArrayModel(obj, this)
            is Clob -> ClobModel(obj, this)
            is Struct -> StructModel(obj, this)
            is FetchedResultSet -> FetchedResultSetModel(obj, this)
            is FetchedResultSetTransposed -> FetchedResultSetTransposedModel(obj, this)
            else -> super.handleUnknownType(obj)
        }

    }
}
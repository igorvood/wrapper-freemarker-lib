package ru.vood.freemarker.ext.sql

import ru.vood.freemarker.ext.sql.ColumnMeta
import java.sql.ResultSet
import java.util.*
import java.util.stream.Collectors

/**
 * This class contains the static result of fetching from a [ResultSet] and its metadata. This is needed because
 * the original result set can be fetched only once, so wrapping it directly may lead to getting empty result set in
 * FTL if the object is accessed twice.
 */
class FetchedResultSet internal constructor(rs: ResultSet, ftlDefaultObjectWrapper: FtlDefaultObjectWrapper) {
    var metaData: List<ColumnMeta>
    var columnIndices: Map<String, Int>
    var rows: List<List<Any>>
    val columnLabels: List<String>
        get() = metaData.stream().map(ColumnMeta::columnLabel).collect(Collectors.toList())

    fun getColumnIndex(label: String): Int {
        return columnIndices[label]!!
    }

    fun findColumn(label: String): Int? {
        val index = getColumnIndex(label)
        return index + 1
    }

    init {
        try {
            metaData = Collections.unmodifiableList(ColumnMeta.of(rs.metaData))
            val columnCount = metaData.size
            val tempColumnIndices: MutableMap<String, Int> = HashMap(columnCount)
            for (i in 0 until columnCount) {
                val columnLabel = metaData[i].columnLabel
                // if two columns have same labels, save only the 1st one
                tempColumnIndices.putIfAbsent(columnLabel, i)
            }
            val tempRows: MutableList<List<Any>> = ArrayList(64)
            while (rs.next()) {
                val row: MutableList<Any> = ArrayList(columnCount)
                for (i in 0 until columnCount) {
                    var o = rs.getObject(i + 1)
                    if (o is ResultSet) {
                        o = FetchedResultSet(o, ftlDefaultObjectWrapper)
                    }
                    row.add(o)
                }
                tempRows.add(Collections.unmodifiableList(row))
            }
            rows = Collections.unmodifiableList(tempRows)
            columnIndices = Collections.unmodifiableMap(tempColumnIndices)
        } finally {
            if (!rs.isClosed) {
                rs.close()
            }
        }
    }
}
package ru.vood.freemarker.ext.sql

import java.sql.ResultSetMetaData
import java.util.stream.IntStream
import kotlin.streams.toList

class ColumnMeta private constructor(
        val isAutoIncrement: Boolean,
        val nullable: Int,
        val isSigned: Boolean,
        val columnLabel: String,
        val columnName: String,
        val precision: Int,
        val scale: Int,
        val columnType: Int,
        val columnTypeName: String,
        val isReadOnly: Boolean,
        val isWritable: Boolean,
        val isDefinitelyWritable: Boolean,
        val columnClassName: String) {


    companion object {
        @JvmStatic
        fun of(m: ResultSetMetaData): List<ColumnMeta> {
            val toList = IntStream.range(1, m.columnCount + 1)
                    .mapToObj {
                        ColumnMeta(
                                m.isAutoIncrement(it), m.isNullable(it), m.isSigned(it), m.getColumnLabel(it), m.getColumnName(it),
                                m.getPrecision(it), m.getScale(it), m.getColumnType(it), m.getColumnTypeName(it),
                                m.isReadOnly(it), m.isWritable(it), m.isDefinitelyWritable(it), m.getColumnClassName(it))
                    }
                    .toList<ColumnMeta>()
            return toList
        }
    }
}
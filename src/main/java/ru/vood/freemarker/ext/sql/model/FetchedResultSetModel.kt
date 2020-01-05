package ru.vood.freemarker.ext.sql.model

import freemarker.core.CollectionAndSequence
import freemarker.ext.beans.BeanModel
import freemarker.ext.beans.BeansWrapper
import freemarker.template.*
import org.springframework.util.Assert
import ru.vood.freemarker.ext.sql.FetchedResultSet
import ru.vood.freemarker.ext.sql.FetchedResultSetTransposed
import java.util.*
import java.util.function.Consumer

/**
 * This class wraps [FetchedResultSet] and adapts it for using in FTL both as a sequence of rows and as a bean.
 */
class FetchedResultSetModel(frs: FetchedResultSet, objectWrapper: BeansWrapper) : BeanModel(frs, objectWrapper), TemplateSequenceModel, TemplateScalarModel {
    private val objectWrapper: ObjectWrapper
    private val frs: FetchedResultSet

    init {
        this.objectWrapper = objectWrapper
        this.frs = frs
    }

    companion object {
        private const val TRANSPOSE_METHOD_NAME = "transpose"
    }

    override fun get(index: Int): TemplateModel = FetchedResultSetRowModel(frs, index, objectWrapper)

    override fun size(): Int = frs.rows.size

    override fun get(key: String): TemplateModel =
            if (key == TRANSPOSE_METHOD_NAME) {
                transpose()
            } else super.get(key)

    private fun transpose(): TemplateMethodModelEx =
            TemplateMethodModelEx { args: List<*> ->
                Assert.isTrue(args.isEmpty()) { "No arguments needed, but args=$args" }
                wrap(FetchedResultSetTransposed(frs))
            }

    override fun keys(): TemplateCollectionModel {
        val keySetEx = super.keySet()
        keySetEx.add(TRANSPOSE_METHOD_NAME)
        return CollectionAndSequence(SimpleSequence(keySetEx, objectWrapper))
    }

    override fun values(): TemplateCollectionModel = SimpleCollection(ArrayList<Any>(0), objectWrapper)

    override fun getAsString(): String {
        val sb = StringBuilder()
        frs.columnLabels.forEach(Consumer { cl: String -> sb.append(cl).append('\t') })
        sb.append('\n')
        frs.rows.forEach(Consumer { row: List<Any> ->
            row.forEach(Consumer { columnVal: Any -> sb.append(columnVal).append('\t') })
            sb.append('\n')
        })
        return sb.toString()
    }

}
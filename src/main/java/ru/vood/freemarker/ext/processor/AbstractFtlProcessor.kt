package ru.vood.freemarker.ext.processor

import freemarker.ext.beans.BeansWrapperBuilder
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateMethodModelEx
import freemarker.template.TemplateScalarModel
import org.springframework.util.Assert
import ru.vood.freemarker.ext.sql.SqlFtlException
import java.io.IOException

abstract class AbstractFtlProcessor() : Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS), ProcessFtl {

    override fun getTemplate(templateName: String): Template {
        return try {
            super.getTemplate(templateName)
        } catch (e: IOException) {
            throw SqlFtlException("Unable to get template '$templateName'", e)
        }
    }

    open fun getTemplateFromString(templateName: String?, templateBody: String?): Template? {
        requireNotNull(templateBody) { "Template body is null" }
        val template: Template
        template = try {
            Template(templateName, templateBody, this)
        } catch (e: IOException) {
            throw SqlFtlException("Unable to create template from pure ftl text", e)
        }
        return template
    }

    protected open fun getGetStaticMethod(): TemplateMethodModelEx {
        return TemplateMethodModelEx { args: List<*> ->
            Assert.isTrue(args.size == 1) { "Wrong number of arguments: expected 1, got " + args.size }
            val classNameObj = args[0]!!
            Assert.isTrue(
                    classNameObj is TemplateScalarModel
            ) { "Illegal type of argument #1: expected string, got " + classNameObj.javaClass.name }
            BeansWrapperBuilder(incompatibleImprovements).build()
                    .staticModels[(classNameObj as TemplateScalarModel).asString]
        }
    }


}
package ru.vood.freemarker.ext.processor

import freemarker.template.Configuration

abstract class AbstractFtlProcessor() : Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS), ProcessFtl {

}
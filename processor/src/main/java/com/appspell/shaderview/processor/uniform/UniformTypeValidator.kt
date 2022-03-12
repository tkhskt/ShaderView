package com.appspell.shaderview.processor.uniform

import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.ClassName

class UniformTypeValidator(
    private val logger: KSPLogger,
) {

    private val uniformTypeNames = UniformType.values().map { it.className }

    fun validate(className: ClassName) {
        if (uniformTypeNames.none { it == className }) {
            logger.error("${className.canonicalName} is not a type that can be used in ShaderView")
        }
    }
}

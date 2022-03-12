package com.appspell.shaderview.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class ShaderParametersProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ShaderParametersProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
    }
}

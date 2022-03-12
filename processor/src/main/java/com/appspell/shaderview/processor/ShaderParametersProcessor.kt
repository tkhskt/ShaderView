package com.appspell.shaderview.processor

import com.appspell.shaderview.processor.ClassNames.SHADER_PARAMETERS
import com.appspell.shaderview.processor.generator.*
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview

class ShaderParametersProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private var invoked = false

    @OptIn(KotlinPoetKspPreview::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            return emptyList()
        }
        val symbols =
            resolver.getSymbolsWithAnnotation(SHADER_PARAMETERS.canonicalName)
        val shaderParametersBuilderGenerator =
            ShaderParametersBuilderGenerator(codeGenerator, logger)
        val shaderViewExtensionGenerator =
            ShaderViewExtensionGenerator(codeGenerator)
        val shaderParameterUpdaterGenerator =
            ShaderParametersUpdaterGenerator(codeGenerator, logger)
        symbols.filterIsInstance<KSClassDeclaration>().forEach { classDeclaration ->
            shaderParametersBuilderGenerator.generate(classDeclaration)
            shaderViewExtensionGenerator.generate(classDeclaration)
            shaderParameterUpdaterGenerator.generate(classDeclaration)
        }
        invoked = true
        return emptyList()
    }

    override fun finish() {
    }
}

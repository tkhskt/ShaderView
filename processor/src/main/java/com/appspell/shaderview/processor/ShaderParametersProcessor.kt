package com.appspell.shaderview.processor

import com.appspell.shaderview.processor.ClassNames.SHADER_PARAMETERS
import com.appspell.shaderview.processor.generator.ShaderParametersBuilderGenerator
import com.appspell.shaderview.processor.generator.ShaderParametersUpdaterGenerator
import com.appspell.shaderview.processor.generator.ShaderViewExtensionGenerator
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview

class ShaderParametersProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    @OptIn(KotlinPoetKspPreview::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols =
            resolver.getSymbolsWithAnnotation(SHADER_PARAMETERS.canonicalName)
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(ShaderParametersVisitor(), Unit) }
        return ret
    }

    inner class ShaderParametersVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val shaderParametersBuilderGenerator =
                ShaderParametersBuilderGenerator(codeGenerator, logger)
            val shaderViewExtensionGenerator =
                ShaderViewExtensionGenerator(codeGenerator)
            val shaderParameterUpdaterGenerator =
                ShaderParametersUpdaterGenerator(codeGenerator, logger)
            shaderParametersBuilderGenerator.generate(classDeclaration)
            shaderViewExtensionGenerator.generate(classDeclaration)
            shaderParameterUpdaterGenerator.generate(classDeclaration)
        }
    }
}

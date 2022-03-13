package com.appspell.shaderview.processor.generator

import com.appspell.shaderview.processor.ClassNames.SHADER_VIEW
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo
import java.util.*

@OptIn(KotlinPoetKspPreview::class)
class ShaderViewExtensionGenerator(
    private val codeGenerator: CodeGenerator,
) {

    fun generate(classDeclaration: KSClassDeclaration) {
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.toString()
        generateShaderViewExtension(packageName, className, classDeclaration.containingFile!!)
    }

    private fun generateShaderViewExtension(
        packageName: String,
        parameterClassName: String,
        containingFile: KSFile,
    ) {
        val file = FileSpec.builder(packageName, "ShaderViewExtension")
            .addShaderParametersFunction(parameterClassName)
            .addInitializeFunction(parameterClassName)
            .build()

        file.writeTo(codeGenerator, Dependencies(false, containingFile))
    }

    private fun FileSpec.Builder.addShaderParametersFunction(parameterClassName: String): FileSpec.Builder {
        val builderClassName = ClassName(packageName, "${parameterClassName}Builder")
        val initializerTypeName = LambdaTypeName.get(
            receiver = builderClassName,
            returnType = Unit::class.asTypeName(),
        )
        addFunction(
            FunSpec.builder(parameterClassName.replaceFirstChar { it.lowercase(Locale.getDefault()) })
                .receiver(SHADER_VIEW)
                .addParameter("initializer", initializerTypeName)
                .addCode(
                    """
                    val parameters = $builderClassName().run {
                        initializer()
                        build()
                    }
                    shaderParams = parameters
                    """.trimIndent()
                )
                .build()
        )
        return this
    }

    private fun FileSpec.Builder.addInitializeFunction(parameterClassName: String): FileSpec.Builder {
        val updaterClassName = ClassName(packageName, "${parameterClassName}Updater")
        val initializerTypeName = LambdaTypeName.get(
            receiver = updaterClassName,
            returnType = Unit::class.asTypeName(),
        )
        addFunction(
            FunSpec.builder("onDrawFrame")
                .receiver(SHADER_VIEW)
                .addParameter("listener", initializerTypeName)
                .addCode(
                    """
                    onDrawFrameListener = { shaderParams ->
                        val updater = $updaterClassName(shaderParams)
                        updater.listener()
                    }
                    """.trimIndent()
                )
                .build()
        )
        return this
    }
}

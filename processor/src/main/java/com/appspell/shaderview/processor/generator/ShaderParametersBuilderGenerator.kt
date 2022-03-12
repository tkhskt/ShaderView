package com.appspell.shaderview.processor.generator

import com.appspell.shaderview.processor.ClassNames.GL_TEXTURE0
import com.appspell.shaderview.processor.ClassNames.SHADER_PARAMS
import com.appspell.shaderview.processor.ClassNames.SHADER_PARAMS_BUILDER
import com.appspell.shaderview.processor.uniform.UniformType
import com.appspell.shaderview.processor.uniform.UniformTypeValidator
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

@OptIn(KotlinPoetKspPreview::class)
class ShaderParametersBuilderGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) {

    fun generate(classDeclaration: KSClassDeclaration) {
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.toString()
        val properties = classDeclaration.getAllProperties()
        generateShaderParameterBuilder(packageName, className, properties)
    }

    private fun generateShaderParameterBuilder(
        packageName: String,
        parameterClassName: String,
        properties: Sequence<KSPropertyDeclaration>,
    ) {
        val builderClassName = "${parameterClassName}Builder"
        val file = FileSpec.builder(packageName, builderClassName)
            .addType(
                TypeSpec.classBuilder(builderClassName)
                    .addProperty()
                    .addFunctions(properties)
                    .build()
            ).build()
        file.writeTo(codeGenerator, Dependencies(false))
    }

    private fun TypeSpec.Builder.addProperty(): TypeSpec.Builder {
        addProperty(
            PropertySpec.builder(
                "shaderParamsBuilder",
                SHADER_PARAMS_BUILDER,
                KModifier.PRIVATE
            )
                .initializer("ShaderParamsBuilder()")
                .build()
        )
        return this
    }

    private fun TypeSpec.Builder.addFunctions(properties: Sequence<KSPropertyDeclaration>): TypeSpec.Builder {
        properties.forEach { property ->
            val resolvedType = property.type.resolve()
            val packageName = resolvedType.declaration.packageName.asString()
            val simpleName = resolvedType.declaration.simpleName.asString()
            val typeClassName = ClassName(packageName, simpleName)
            UniformTypeValidator(logger).validate(typeClassName)
            addFunction(
                FunSpec.builder(property.qualifiedName!!.getShortName())
                    .addFunctionParameters(property.type.toTypeName(), typeClassName)
                    .addFunctionCode(property.simpleName.asString(), typeClassName)
                    .build()
            )
        }
        addFunction(
            FunSpec.builder("build")
                .returns(SHADER_PARAMS)
                .addStatement("return shaderParamsBuilder.build()")
                .build()
        )
        return this
    }

    private fun FunSpec.Builder.addFunctionParameters(
        typeName: TypeName,
        typeClassName: ClassName,
    ): FunSpec.Builder {
        addParameter(
            "uniform",
            typeName.copy(nullable = true),
        )
        if (UniformType.classNameOf(typeClassName) == UniformType.TEXTURE_2D) {
            addParameter(
                ParameterSpec.builder("textureSlot", Int::class)
                    .defaultValue(GL_TEXTURE0.canonicalName)
                    .build()
            )
        }
        return this
    }

    private fun FunSpec.Builder.addFunctionCode(
        paramName: String,
        typeClassName: ClassName,
    ): FunSpec.Builder {
        when (UniformType.classNameOf(typeClassName)) {
            UniformType.FLOAT -> {
                addStatement("shaderParamsBuilder.addFloat(\"$paramName\", uniform)")
            }
            UniformType.INT -> {
                addStatement("shaderParamsBuilder.addInt(\"$paramName\", uniform)")
            }
            UniformType.TEXTURE_2D -> {
                addCode(
                    """
                    val textureResourceId = uniform?.textureResourceId
                    if (uniform == null || textureResourceId == null) {
                        shaderParamsBuilder.addTexture2D("$paramName", uniform?.bitmap, textureSlot)
                        return
                    }
                    shaderParamsBuilder.addTexture2D("$paramName", textureResourceId, textureSlot)
                    """.trimIndent()
                )
            }
            UniformType.COLOR -> {
                addCode(
                    """
                    if (uniform?.colorRes != null && uniform?.resources != null) {
                      shaderParamsBuilder.addColor("$paramName", uniform.colorRes, uniform.resources)
                    } else if (uniform?.color != null) {
                      shaderParamsBuilder.addColor("$paramName", uniform.color)
                    }
                    """.trimIndent()
                )
            }
            UniformType.VEC_2F -> {
                addStatement("shaderParamsBuilder.addVec2f(\"$paramName\", uniform?.toArray())")
            }
            UniformType.VEC_3F -> {
                addStatement("shaderParamsBuilder.addVec3f(\"$paramName\", uniform?.toArray())")
            }
            UniformType.VEC_4F -> {
                addStatement("shaderParamsBuilder.addVec4f(\"$paramName\", uniform?.toArray())")
            }
            UniformType.VEC_2I -> {
                addStatement("shaderParamsBuilder.addVec2i(\"$paramName\", uniform?.toArray())")
            }
            UniformType.VEC_3I -> {
                addStatement("shaderParamsBuilder.addVec3i(\"$paramName\", uniform?.toArray())")
            }
            UniformType.VEC_4I -> {
                addStatement("shaderParamsBuilder.addVec4i(\"$paramName\", uniform?.toArray())")
            }
        }
        return this
    }
}

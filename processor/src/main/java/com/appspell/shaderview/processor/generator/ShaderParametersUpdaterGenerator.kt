package com.appspell.shaderview.processor.generator

import com.appspell.shaderview.processor.ClassNames.RESOURCES
import com.appspell.shaderview.processor.ClassNames.SHADER_PARAMS
import com.appspell.shaderview.processor.ClassNames.SHADER_PARAMS_BUILDER
import com.appspell.shaderview.processor.uniform.UniformType
import com.appspell.shaderview.processor.uniform.UniformTypeValidator
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

@OptIn(KotlinPoetKspPreview::class)
class ShaderParametersUpdaterGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) {

    fun generate(classDeclaration: KSClassDeclaration) {
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.toString()
        val properties = classDeclaration.getAllProperties()
        generateShaderParameterUpdater(
            packageName,
            className,
            properties,
            classDeclaration.containingFile!!
        )
    }

    private fun generateShaderParameterUpdater(
        packageName: String,
        parameterClassName: String,
        properties: Sequence<KSPropertyDeclaration>,
        containingFile: KSFile,
    ) {
        val builderClassName = "${parameterClassName}Updater"
        val constructor = FunSpec.constructorBuilder()
            .addParameter("shaderParams", SHADER_PARAMS)
            .build()
        val file = FileSpec.builder(packageName, builderClassName)
            .addType(
                TypeSpec.classBuilder(builderClassName)
                    .primaryConstructor(constructor)
                    .addProperty(
                        PropertySpec.builder("shaderParams", SHADER_PARAMS)
                            .initializer("shaderParams")
                            .addModifiers(KModifier.PRIVATE)
                            .build()
                    )
                    .addFunctions(properties)
                    .build()
            ).build()
        file.writeTo(codeGenerator, Dependencies(false, containingFile))
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
        addShaderParamsFunctions()
        return this
    }

    private fun FunSpec.Builder.addFunctionParameters(
        typeName: TypeName,
        typeClassName: ClassName,
    ): FunSpec.Builder {
        addParameter("uniform", typeName)
        if (UniformType.classNameOf(typeClassName) == UniformType.TEXTURE_2D) {
            addParameter(
                ParameterSpec.builder("needToRecycleWhenUploaded", Boolean::class)
                    .defaultValue("false")
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
            UniformType.FLOAT,
            UniformType.INT -> {
                addStatement("shaderParams.updateValue(\"$paramName\", uniform)")
            }
            UniformType.COLOR -> {
                addCode(
                    """
                    if (uniform.colorRes != null) {
                        shaderParams.updateValue("$paramName", uniform.colorRes)
                    } else if(uniform.color != null) {
                        shaderParams.updateValue("$paramName", uniform.color)
                    }
                    """.trimIndent()
                )
            }
            UniformType.TEXTURE_2D -> {
                addCode(
                    """
                    val textureResourceId = uniform.textureResourceId
                    val bitmap = uniform.bitmap
                    if (textureResourceId != null) {
                        shaderParams.updateValue2D("$paramName", textureResourceId)
                    } else if (bitmap != null) {
                        shaderParams.updateValue2D("$paramName", bitmap, needToRecycleWhenUploaded)
                    }
                    """.trimIndent()
                )
            }
            else -> {
                addStatement("shaderParams.updateValue(\"$paramName\", uniform.toArray())")
            }
        }
        return this
    }

    private fun TypeSpec.Builder.addShaderParamsFunctions(): TypeSpec.Builder {
        addFunction(
            FunSpec.builder("getParamShaderLocation")
                .addParameter("paramName", String::class)
                .returns(
                    Int::class.asTypeName().copy(
                        nullable = true
                    )
                )
                .addStatement("return shaderParams.getParamShaderLocation(paramName)")
                .build()
        )
        addFunction(
            FunSpec.builder("getParamValue")
                .addParameter("paramName", String::class)
                .returns(
                    Any::class.asTypeName().copy(
                        nullable = true
                    )
                )
                .addStatement("return shaderParams.getParamValue(paramName)")
                .build()
        )
        addFunction(
            FunSpec.builder("pushValuesToProgram")
                .addStatement("shaderParams.pushValuesToProgram()")
                .build()
        )
        addFunction(
            FunSpec.builder("bindParams")
                .addParameter("shaderProgram", Int::class)
                .addParameter("resources", RESOURCES)
                .addStatement("shaderParams.bindParams(shaderProgram, resources)")
                .build()
        )
        addFunction(
            FunSpec.builder("release")
                .addStatement("shaderParams.release()")
                .build()
        )
        addFunction(
            FunSpec.builder("newBuilder")
                .returns(SHADER_PARAMS_BUILDER)
                .addStatement("return shaderParams.newBuilder()")
                .build()
        )
        return this
    }
}

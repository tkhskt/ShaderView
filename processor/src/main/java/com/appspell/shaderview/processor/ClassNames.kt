package com.appspell.shaderview.processor

import com.squareup.kotlinpoet.ClassName

object ClassNames {
    private const val PKG_SHADER_VIEW = "com.appspell.shaderview"
    private const val PKG_SHADER_VIEW_GL_PARAMS = "com.appspell.shaderview.gl.params"
    private const val PKG_SHADER_VIEW_KTX_ANNOTATION = "com.appspell.shaderview.ktx.annotation"
    private const val PKG_SHADER_VIEW_KTX_UNIFORM = "com.appspell.shaderview.ktx.uniform"
    private const val PKG_ANDROID_CONTENT_RES = "android.content.res"
    private const val PKG_KOTLIN = "kotlin"
    private const val PKG_ANDROID_OPENGL_GLES30 = "android.opengl.GLES30"


    val SHADER_VIEW = ClassName(PKG_SHADER_VIEW, "ShaderView")
    val SHADER_PARAMS = ClassName(PKG_SHADER_VIEW_GL_PARAMS, "ShaderParams")
    val SHADER_PARAMS_BUILDER = ClassName(PKG_SHADER_VIEW_GL_PARAMS, "ShaderParamsBuilder")
    val SHADER_PARAMETERS = ClassName(PKG_SHADER_VIEW_KTX_ANNOTATION, "ShaderParameters")

    val FLOAT = ClassName(PKG_KOTLIN, "Float")
    val INT = ClassName(PKG_KOTLIN, "Int")
    val TEXTURE_2D = ClassName(PKG_SHADER_VIEW_KTX_UNIFORM, "Texture2D")
    val COLOR = ClassName(PKG_SHADER_VIEW_KTX_UNIFORM, "Color")
    val VEC_2F = ClassName(PKG_SHADER_VIEW_KTX_UNIFORM, "Vec2f")
    val VEC_3F = ClassName(PKG_SHADER_VIEW_KTX_UNIFORM, "Vec3f")
    val VEC_4F = ClassName(PKG_SHADER_VIEW_KTX_UNIFORM, "Vec4f")
    val VEC_2I = ClassName(PKG_SHADER_VIEW_KTX_UNIFORM, "Vec2i")
    val VEC_3I = ClassName(PKG_SHADER_VIEW_KTX_UNIFORM, "Vec3i")
    val VEC_4I = ClassName(PKG_SHADER_VIEW_KTX_UNIFORM, "Vec4i")

    val RESOURCES = ClassName(PKG_ANDROID_CONTENT_RES, "Resources")

    val GL_TEXTURE0 = ClassName(PKG_ANDROID_OPENGL_GLES30, "GL_TEXTURE0")
}

package com.appspell.shaderview.demo.distortion

import com.appspell.shaderview.ktx.annotation.ShaderParameters
import com.appspell.shaderview.ktx.uniform.Texture2D
import com.appspell.shaderview.ktx.uniform.Vec2f
import com.appspell.shaderview.ktx.uniform.Vec4f

@ShaderParameters
data class DistortTextureShaderParameters(
    val uPointer: Vec2f,
    val uTexture: Texture2D,
    val uVelo: Float,
    val resolution: Vec4f,
)

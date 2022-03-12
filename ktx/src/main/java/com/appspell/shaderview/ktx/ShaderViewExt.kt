package com.appspell.shaderview.ktx

import com.appspell.shaderview.ShaderView

fun ShaderView.initialize(initializer: ShaderBuilder.() -> Unit) {
    val builder = ShaderBuilder().apply {
        initializer()
    }
    updateContinuously = builder.updateContinuously
    fragmentShaderRawResId = builder.fragmentShaderRawResId
    vertexShaderRawResId = builder.vertexShaderRawResId
    onDrawFrameListener = builder.onDrawFrameListener
}

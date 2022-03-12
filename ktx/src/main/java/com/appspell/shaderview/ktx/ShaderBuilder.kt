package com.appspell.shaderview.ktx

import androidx.annotation.RawRes
import com.appspell.shaderview.R
import com.appspell.shaderview.gl.params.ShaderParams

class ShaderBuilder {
    var onDrawFrameListener: (ShaderParams) -> Unit = {}
    var updateContinuously: Boolean = false

    var fragmentShaderRawResId: Int = R.raw.quad_vert
    var vertexShaderRawResId: Int = R.raw.default_frag

    fun onDrawFrameListener(listener: (ShaderParams) -> Unit) {
        this.onDrawFrameListener = listener
    }

    fun updateContinuously(value: Boolean) {
        updateContinuously = value
    }

    fun vertexShaderRawResId(@RawRes value: Int) {
        vertexShaderRawResId = value
    }

    fun fragmentShaderRawResId(@RawRes value: Int) {
        fragmentShaderRawResId = value
    }
}

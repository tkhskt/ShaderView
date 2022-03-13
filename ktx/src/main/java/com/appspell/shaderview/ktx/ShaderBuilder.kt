package com.appspell.shaderview.ktx

import androidx.annotation.RawRes
import com.appspell.shaderview.R
import com.appspell.shaderview.gl.params.ShaderParams
import com.appspell.shaderview.gl.shader.GLShader

class ShaderBuilder {
    var onDrawFrameListener: (ShaderParams) -> Unit = {}
        private set
    var updateContinuously: Boolean = false
        private set

    var fragmentShaderRawResId: Int = R.raw.default_frag
        private set
    var vertexShaderRawResId: Int = R.raw.quad_vert
        private set

    var onViewReadyListener: ((shader: GLShader) -> Unit)? = null
        private set
    var debugMode: Boolean = false
        private set

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

    fun debugMode(debugMode: Boolean) {
        this.debugMode = debugMode
    }

    fun onViewReadyListener(listener: (GLShader) -> Unit) {
        this.onViewReadyListener = listener
    }
}

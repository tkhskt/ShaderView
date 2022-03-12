package com.appspell.shaderview.ktx.uniform

data class Vec2f(
    val x: Float,
    val y: Float,
) {
    fun toArray(): FloatArray = floatArrayOf(x, y)
}

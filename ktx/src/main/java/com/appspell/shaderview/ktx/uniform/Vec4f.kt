package com.appspell.shaderview.ktx.uniform

data class Vec4f(
    val x: Float,
    val y: Float,
    val z: Float,
    val w: Float,
) {
    fun toArray(): FloatArray = floatArrayOf(x, y, z, w)
}

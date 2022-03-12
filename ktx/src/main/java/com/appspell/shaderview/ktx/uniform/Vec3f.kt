package com.appspell.shaderview.ktx.uniform

data class Vec3f(
    val x: Float,
    val y: Float,
    val z: Float,
) {
    fun toArray(): FloatArray = floatArrayOf(x, y, z)
}

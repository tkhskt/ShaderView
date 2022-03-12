package com.appspell.shaderview.ktx.uniform

data class Vec3i(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    fun toArray(): IntArray = intArrayOf(x, y, z)
}
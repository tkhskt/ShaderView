package com.appspell.shaderview.ktx.uniform

data class Vec4i(
    val x: Int,
    val y: Int,
    val z: Int,
    val w: Int,
) {
    fun toArray(): IntArray = intArrayOf(x, y, z, w)
}

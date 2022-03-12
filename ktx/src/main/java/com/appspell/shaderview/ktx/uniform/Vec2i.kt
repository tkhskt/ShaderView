package com.appspell.shaderview.ktx.uniform

data class Vec2i(
    val x: Int,
    val y: Int,
) {
    fun toArray(): IntArray = intArrayOf(x, y)
}

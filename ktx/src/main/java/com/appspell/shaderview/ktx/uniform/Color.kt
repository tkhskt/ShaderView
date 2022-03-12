package com.appspell.shaderview.ktx.uniform

import android.content.res.Resources
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

data class Color(
    @ColorRes val colorRes: Int?,
    @ColorInt val color: Int?,
    val resources: Resources?,
)

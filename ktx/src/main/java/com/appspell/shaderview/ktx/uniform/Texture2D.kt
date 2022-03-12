package com.appspell.shaderview.ktx.uniform

import android.graphics.Bitmap
import android.opengl.GLES30
import androidx.annotation.DrawableRes

data class Texture2D(
    val bitmap: Bitmap? = null,
    @DrawableRes val textureResourceId: Int? = null,
    val textureSlot: Int = GLES30.GL_TEXTURE0,
)

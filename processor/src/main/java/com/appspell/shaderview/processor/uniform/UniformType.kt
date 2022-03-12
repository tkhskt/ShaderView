package com.appspell.shaderview.processor.uniform

import com.appspell.shaderview.processor.ClassNames
import com.squareup.kotlinpoet.ClassName

enum class UniformType(val className: ClassName) {
    FLOAT(ClassNames.FLOAT),
    INT(ClassNames.INT),
    TEXTURE_2D(ClassNames.TEXTURE_2D),
    COLOR(ClassNames.COLOR),
    VEC_2F(ClassNames.VEC_2F),
    VEC_3F(ClassNames.VEC_3F),
    VEC_4F(ClassNames.VEC_4F),
    VEC_2I(ClassNames.VEC_2I),
    VEC_3I(ClassNames.VEC_3I),
    VEC_4I(ClassNames.VEC_4I);

    companion object {
        fun classNameOf(className: ClassName): UniformType {
            return values().first {
                it.className == className
            }
        }
    }
}

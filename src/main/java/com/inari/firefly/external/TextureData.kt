package com.inari.firefly.external

import com.inari.firefly.IntFunction
import com.inari.firefly.NO_NAME

class TextureData(
    @JvmField var resourceName: String = NO_NAME,
    @JvmField var isMipmap: Boolean = false,
    @JvmField var wrapS: Int = 0,
    @JvmField var wrapT: Int = 0,
    @JvmField var minFilter: Int = 0,
    @JvmField var magFilter: Int = 0,
    @JvmField var colorConverter: IntFunction = IntFunction.identity()
) {

    fun reset() {
        resourceName = NO_NAME
        isMipmap = false
        wrapS = 0
        wrapT = 0
        minFilter = 0
        magFilter = 0
        colorConverter = IntFunction.identity()
    }

}

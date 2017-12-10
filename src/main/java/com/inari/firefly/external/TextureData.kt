package com.inari.firefly.external

import com.inari.firefly.IntFunction

interface TextureData {

    val resourceName: String
    val isMipmap: Boolean

    val wrapS: Int
    val wrapT: Int
    val minFilter: Int
    val magFilter: Int

    val colorConverter: IntFunction
}

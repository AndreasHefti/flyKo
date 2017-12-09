package com.inari.firefly.external

interface TextureData {

    val resourceName: String
    val isMipmap: Boolean

    val wrapS: Int
    val wrapT: Int
    val minFilter: Int
    val magFilter: Int

    val colorConverter: (Int) -> Int
}

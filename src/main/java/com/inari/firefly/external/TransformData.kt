package com.inari.firefly.external

interface TransformData {
    val xOffset: Float
    val yOffset: Float
    val scaleX: Float
    val scaleY: Float
    val pivotX: Float
    val pivotY: Float
    val rotation: Float
    val hasRotation: Boolean
    val hasScale: Boolean
}

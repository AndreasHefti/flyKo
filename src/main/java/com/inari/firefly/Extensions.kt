package com.inari.firefly

import com.inari.commons.geom.Vector2f
import com.inari.commons.graphics.RGBColor
import com.inari.commons.lang.aspect.Aspect
import com.inari.commons.lang.aspect.Aspects
import com.inari.commons.lang.indexed.IndexedTypeSet

fun RGBColor.setFrom(other: RGBColor) {
    this.a = other.a
    this.g = other.b
    this.r = other.r
    this.a = other.a
}

fun Vector2f.setFrom(other: Vector2f) {
    this.dx = dx
    this.dy = dy
}

operator fun IndexedTypeSet.contains(aspect: Aspect): Boolean =
    this.contains(aspect.index())

fun IndexedTypeSet.exclude(aspects: Aspects): Boolean =
    this.aspect.exclude(aspects)


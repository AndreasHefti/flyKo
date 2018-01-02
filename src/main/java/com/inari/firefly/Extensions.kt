package com.inari.firefly

import com.inari.commons.geom.Position
import com.inari.commons.geom.PositionF
import com.inari.commons.geom.Rectangle
import com.inari.commons.geom.Vector2f
import com.inari.commons.graphics.RGBColor
import com.inari.commons.lang.aspect.Aspect
import com.inari.commons.lang.aspect.Aspects
import com.inari.commons.lang.indexed.IndexedTypeSet

fun RGBColor.setFrom(other: RGBColor) {
    this.r = other.r
    this.g = other.g
    this.b = other.b
    this.a = other.a
}

operator fun RGBColor.invoke(
    r: Float = this.r,
    g: Float = this.g,
    b: Float = this.b,
    a: Float = this.a
) {
    this.r = r
    this.g = g
    this.b = b
    this.a = a
}

fun Vector2f.setFrom(other: Vector2f) {
    this.dx = other.dx
    this.dy = other.dy
}

operator fun Vector2f.invoke(dx: Float = this.dx, dy: Float = this.dy) {
    this.dx = dx
    this.dy = dy
}

operator fun Position.invoke(x: Int = this.x, y: Int = this.y) {
    this.x = x
    this.y = y
}

operator fun PositionF.invoke(x: Float = this.x, y: Float = this.y) {
    this.x = x
    this.y = y
}

operator fun Rectangle.invoke(
    x: Int = this.x,
    y: Int = this.y,
    width: Int = this.width,
    height: Int = this.height
) {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}

operator fun IndexedTypeSet.contains(aspect: Aspect): Boolean =
    this.contains(aspect.index())

fun IndexedTypeSet.exclude(aspects: Aspects): Boolean =
    this.aspect.exclude(aspects)






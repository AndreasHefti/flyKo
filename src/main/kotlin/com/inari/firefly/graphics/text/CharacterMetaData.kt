package com.inari.firefly.graphics.text

import com.inari.firefly.component.ComponentDSL
import com.inari.firefly.external.TransformData
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.particle.Particle
import com.inari.firefly.graphics.particle.SpriteParticle
import com.inari.util.geom.PositionF
import com.inari.util.geom.Vector2f
import com.inari.util.graphics.RGBColor

@ComponentDSL
class CharacterMetaData private constructor(){

    @JvmField internal val transformData = TransformData()
    @JvmField internal val tint = RGBColor(1f, 1f, 1f, 1f)
    @JvmField internal var blend = BlendMode.NONE

    val ff_Position: PositionF
        get() = transformData.position
    val ff_Pivot: PositionF
        get() = transformData.pivot
    val ff_Scale: Vector2f
        get() = transformData.scale
    var ff_Rotation: Float
        get() = transformData.rotation
        set(value) { transformData.rotation = value }
    var ff_Tint: RGBColor
        get() = tint
        set(value) { tint(value) }
    var ff_Blend: BlendMode
        get() = blend
        set(value) {blend = value}

    companion object {
        val of: (CharacterMetaData.() -> Unit) -> CharacterMetaData = { configure ->
            val instance = CharacterMetaData()
            instance.also(configure)
            instance
        }
    }
}
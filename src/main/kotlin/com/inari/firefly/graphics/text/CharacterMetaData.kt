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

    val position: PositionF
        get() = transformData.position
    val pivot: PositionF
        get() = transformData.pivot
    val scale: Vector2f
        get() = transformData.scale
    var rotation: Float
        get() = transformData.rotation
        set(value) { transformData.rotation = value }
    var tint: RGBColor = RGBColor(1f, 1f, 1f, 1f)
    var blend: BlendMode = BlendMode.NONE

    companion object {
        val of: (CharacterMetaData.() -> Unit) -> CharacterMetaData = { configure ->
            val instance = CharacterMetaData()
            instance.also(configure)
            instance
        }
    }
}
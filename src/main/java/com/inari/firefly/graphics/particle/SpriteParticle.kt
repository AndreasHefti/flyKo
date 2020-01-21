package com.inari.firefly.graphics.particle

import com.inari.firefly.asset.AssetInstanceRefResolver
import com.inari.firefly.external.SpriteRenderable
import com.inari.firefly.graphics.BlendMode
import com.inari.firefly.graphics.tile.set.ProtoTile
import com.inari.util.geom.PositionF
import com.inari.util.geom.Vector2f
import com.inari.util.graphics.RGBColor

class SpriteParticle() : Particle() {

    @JvmField internal val spriteRenderable = SpriteRenderable()
    @JvmField internal val spriteRef: Int = -1

    val ff_Sprite = AssetInstanceRefResolver(
            { index -> spriteRenderable.spriteId = index },
            { spriteRenderable.spriteId })
    val ff_Shader = AssetInstanceRefResolver(
            { index -> spriteRenderable.shaderId = index },
            { spriteRenderable.shaderId })
    var ff_Blend: BlendMode
        get() = spriteRenderable.blendMode
        set(value) { spriteRenderable.blendMode = value }
    var ff_Tint: RGBColor
        get() = spriteRenderable.tintColor
        set(value) { spriteRenderable.tintColor(value) }

    companion object : ParticleBuilder<SpriteParticle> {
        override fun createEmpty(): SpriteParticle =
                SpriteParticle()

        val of: (SpriteParticle.() -> Unit) -> SpriteParticle = { configure ->
            val instance = SpriteParticle()
            instance.also(configure)
            instance
        }
    }
}
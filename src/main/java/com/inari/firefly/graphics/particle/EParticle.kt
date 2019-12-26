package com.inari.firefly.graphics.particle

import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.graphics.rendering.Renderer
import com.inari.firefly.graphics.rendering.SpriteParticleRenderer
import com.inari.util.collection.DynArray

class EParticle private constructor() : EntityComponent(EParticle::class.java.name) {

    @JvmField internal var rendererRef = SpriteParticleRenderer.instance.index
    private val particle: DynArray<Particle> = DynArray.of(Particle::class.java)

    var ff_Renderer = ComponentRefResolver(Renderer) { index-> rendererRef = index }
    var ff_Particle = ArrayAccessor(particle)

    @Suppress("UNCHECKED_CAST")
    internal fun <P : Particle> getParticle(): DynArray<P> =
        particle as DynArray<P>

    override fun reset() {
        rendererRef = -1
        particle.clear()
    }

    override fun componentType() =
        EParticle.Companion

    companion object : EntityComponentType<EParticle>(EParticle::class.java) {
        override fun createEmpty() = EParticle()
    }
}
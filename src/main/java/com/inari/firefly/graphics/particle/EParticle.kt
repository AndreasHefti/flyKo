package com.inari.firefly.graphics.particle

import com.inari.commons.lang.list.DynArray
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.graphics.rendering.Renderer

class EParticle private constructor() : EntityComponent() {

    @JvmField internal var rendererRef = -1
    private val particle: DynArray<Particle> = DynArray.create(Particle::class.java)

    var ff_Renderer = ComponentRefResolver(Renderer) { index-> rendererRef =index }
    var ff_Particle = ArrayAccessor(particle)

    @Suppress("UNCHECKED_CAST")
    internal fun <P : Particle> getParticle(): DynArray<P> =
        particle as DynArray<P>

    override fun reset() {
        rendererRef = -1
        particle.clear()
    }

    override fun componentType(): ComponentType<EParticle> =
        EParticle.Companion

    companion object : EntityComponentType<EParticle>() {
        override val indexedTypeKey by lazy { EntityComponent.create(EParticle::class.java) }
        override fun createEmpty() = EParticle()
    }
}
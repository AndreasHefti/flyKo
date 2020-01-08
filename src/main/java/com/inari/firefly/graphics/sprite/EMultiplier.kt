package com.inari.firefly.graphics.sprite

import com.inari.util.geom.PositionF
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.component.ComponentType
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.util.collection.DynArray

class EMultiplier private constructor () : EntityComponent(EMultiplier::class.java.name) {

    @JvmField internal val positions: DynArray<PositionF> =
        DynArray.of(PositionF::class.java)

    val ff_Positions = ArrayAccessor(positions)

    override fun reset() {
        positions.clear()
    }

    override fun componentType(): ComponentType<EMultiplier> = Companion
    companion object : EntityComponentType<EMultiplier>(EMultiplier::class.java) {
        override fun createEmpty() = EMultiplier()
    }
}
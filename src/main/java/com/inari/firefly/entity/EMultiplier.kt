package com.inari.firefly.entity

import com.inari.util.geom.PositionF
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.component.ComponentType
import com.inari.util.collection.DynArray

class EMultiplier private constructor () : EntityComponent(EMultiplier::class.java.name) {

    @JvmField internal val positions: DynArray<PositionF> =
        DynArray.of(PositionF::class.java)

    val ff_Positions = ArrayAccessor(positions)

    override fun reset() {
        positions.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EMultiplier>(EMultiplier::class.java) {
        override fun createEmpty() = EMultiplier()
    }
}
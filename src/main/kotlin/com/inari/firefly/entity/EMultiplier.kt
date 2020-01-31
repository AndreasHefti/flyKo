package com.inari.firefly.entity

import com.inari.firefly.component.ArrayAccessor
import com.inari.util.collection.DynArray
import com.inari.util.geom.PositionF

class EMultiplier private constructor () : EntityComponent(EMultiplier::class.java.name) {

    @JvmField internal val positions: DynArray<PositionF> = DynArray.of()

    val ff_Positions = ArrayAccessor(positions)

    override fun reset() {
        positions.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EMultiplier>(EMultiplier::class.java) {
        override fun createEmpty() = EMultiplier()
    }
}
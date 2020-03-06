package com.inari.firefly.entity

import com.inari.firefly.EMPTY_FLOAT_ARRAY
import com.inari.firefly.component.ArrayAccessor
import com.inari.util.collection.DynArray
import com.inari.util.geom.PositionF

class EMultiplier private constructor () : EntityComponent(EMultiplier::class.java.name) {

    @JvmField internal var positions: FloatArray = EMPTY_FLOAT_ARRAY

    var ff_Positions: FloatArray
            get() = positions
            set(value) { positions = value }

    override fun reset() {
        positions = EMPTY_FLOAT_ARRAY
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EMultiplier>(EMultiplier::class.java) {
        override fun createEmpty() = EMultiplier()
    }
}
package com.inari.firefly.entity

import com.inari.util.collection.DynFloatArray

class EMultiplier private constructor () : EntityComponent(EMultiplier::class.java.name) {

    @JvmField internal var positions: DynFloatArray = DynFloatArray()

    var ff_Positions: DynFloatArray
            get() = positions
            set(value) { positions = value }

    override fun reset() {
        positions.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EMultiplier>(EMultiplier::class.java) {
        override fun createEmpty() = EMultiplier()
    }
}
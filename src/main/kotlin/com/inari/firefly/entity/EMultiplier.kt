package com.inari.firefly.entity

import com.inari.util.collection.DynFloatArray

class EMultiplier private constructor () : EntityComponent(EMultiplier::class.simpleName!!) {

    var positions: DynFloatArray = DynFloatArray()

    override fun reset() {
        positions.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EMultiplier>(EMultiplier::class) {
        override fun createEmpty() = EMultiplier()
    }
}
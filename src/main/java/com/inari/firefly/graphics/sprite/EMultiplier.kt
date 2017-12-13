package com.inari.firefly.graphics.sprite

import com.inari.commons.geom.PositionF
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.entity.EntityComponent

class EMultiplier private constructor () : EntityComponent() {

    @JvmField internal val positions: DynArray<PositionF> =
        DynArray.create(PositionF::class.java)

    val ff_Positions = ArrayAccessor(positions)

    override fun reset() {
        positions.clear()
    }

    override fun indexedTypeKey() = EMultiplier.typeKey

    companion object : EntityComponentType<EMultiplier>() {
        override val typeKey = EntityComponent.createTypeKey(EMultiplier::class.java)
        override fun createEmpty() = EMultiplier()
    }
}
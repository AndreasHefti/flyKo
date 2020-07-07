package com.inari.firefly.graphics.text

import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.util.collection.DynArray

class ETextMeta private constructor() : EntityComponent(ETextMeta::class.java.name) {

    @JvmField internal val metaData: DynArray<CharacterMetaData> = DynArray.of()
    @JvmField internal var resolver: (Int) -> CharacterMetaData? = { index -> metaData[index] }

    var ff_Data = ArrayAccessor(metaData)
    var ff_Resolver: (Int) -> CharacterMetaData?
        get() = resolver
        set(value) { resolver = value }

    override fun reset() {
        metaData.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<ETextMeta>(ETextMeta::class.java) {
        override fun createEmpty() = ETextMeta()
    }
}
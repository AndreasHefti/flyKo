package com.inari.firefly.entity

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.indexed.IndexedTypeSet
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentSingleType
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.Constants
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentBuilder

class Entity: SystemComponent() {

    private val components: IndexedTypeSet = IndexedTypeSet(EntityComponent.Companion.TypeKey::class.java)

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey

    override fun name(): String {
        if (components.contains(EMeta.typeKey.index())) {
            return components.get<EMeta>(EMeta.typeKey.index()).name()
        }

        return Constants.NO_NAME
    }

    fun <C : EntityComponent> get(cBuilder: ComponentType<C>) =
            components.get<C>(cBuilder.typeKey.index())

    fun <C : EntityComponent> with(cBuilder: EntityComponentBuilder<C>, configure: (C.() -> Unit)): CompId =
            cBuilder.builder({ comp: C -> components.set(comp); comp})(configure)

    override fun toString(): String =
            "Entity(name=${name()} " +
            "components=$components)"



    companion object : SystemComponentBuilder<Entity>(), ComponentSingleType<Entity> {
        override val typeKey = SystemComponent.createTypeKey(Entity::class.java)
        override public fun createEmpty(): Entity = Entity()
    }

}
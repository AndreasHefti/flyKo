package com.inari.firefly.entity

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.indexed.IndexedTypeSet
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentBuilder

class Entity : SystemComponent {


    private constructor()
    private val components: IndexedTypeSet = IndexedTypeSet(EntityComponent.Companion.TypeKey::class.java)

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey

    override fun name(): String {
        if (components.contains(EMeta.typeKey.index())) {
            return components.get<EMeta>(EMeta.typeKey.index()).name()
        }

        throw RuntimeException("No name support for entity: " + this)
    }

    fun <C : EntityComponent> get(cBuilder: EntityComponentBuilder<C>) =
            components.get<C>(cBuilder.typeKey.index())

    fun <C : EntityComponent> with(cBuilder: EntityComponentBuilder<C>, configure: (C.() -> Unit)): Int =
            cBuilder.builder({ comp: C -> components.set(comp); comp})(configure)


    companion object : SystemComponentBuilder<Entity> {
        override val typeKey = SystemComponent.createTypeKey(Entity::class.java)
        override fun builder(registry: (Entity) -> Entity): (Entity.() -> Unit) -> Int = {
            configure -> build(Entity(), configure, registry)
        }
    }
}
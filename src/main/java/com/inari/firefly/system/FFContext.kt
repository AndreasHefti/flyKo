package com.inari.firefly.system

import com.inari.commons.event.Event
import com.inari.commons.event.Event.EventTypeKey
import com.inari.commons.event.EventDispatcher
import com.inari.commons.event.IEventDispatcher
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.component.ComponentType
import com.inari.firefly.component.mapping.IComponentMap


object FFContext {

    private val eventDispatcher: IEventDispatcher = EventDispatcher()
    private val componentMaps: MutableMap<IIndexedTypeKey, IComponentMap<*>>  = HashMap()

    fun registerComponentMapper(mapper: IComponentMap<*>) {
        componentMaps.put(mapper.type().typeKey, mapper)
    }

    fun disposeComponentMapper(mapper: IComponentMap<*>) {
        componentMaps.remove(mapper.type().typeKey)
    }

    fun <C : Component> mapper(id: CompId): IComponentMap<C> =
            mapper(id.typeKey)

    fun <C : Component> mapper(type: ComponentType<C>): IComponentMap<C> =
            mapper(type.typeKey)


    @Suppress("UNCHECKED_CAST")
    fun <C : Component> mapper(key: IIndexedTypeKey): IComponentMap<C> {
        if (!componentMaps.containsKey(key)) {
            throw RuntimeException("No Component Mapper registered for type: $key")
        }
        return componentMaps[key] as IComponentMap<C>
    }

    fun <C : Component> get(id: CompId): C =
            mapper<C>(id).get(id.index)


    fun <C : Component> get(cType: ComponentType<C>, name: String): C =
            mapper(cType).get(name)

    fun isActive(id: CompId): Boolean =
            mapper<Component>(id).isActive(id.index)

    fun isActive(cType: ComponentType<*>, name: String): Boolean =
            mapper<Component>(cType.typeKey).isActive(name)

    fun activate(id: CompId): FFContext {
        mapper<Component>(id).activate(id.index)
        return this
    }

    fun activate(cType: ComponentType<*>, name: String): FFContext {
        mapper<Component>(cType.typeKey).activate(name)
        return this
    }

    fun deactivate(id: CompId): FFContext {
        mapper<Component>(id).deactivate(id.index)
        return this
    }

    fun deactivate(cType: ComponentType<*>, name: String): FFContext {
        mapper<Component>(cType.typeKey).deactivate(name)
        return this
    }



    fun <L> registerListener(eventType: EventTypeKey, listener: L): FFContext {
        eventDispatcher.register(eventType, listener)
        return this
    }

    fun <L> disposeListener(eventType: EventTypeKey, listener: L): FFContext {
        eventDispatcher.unregister(eventType, listener)
        return this
    }

    fun <L> notify(event: Event<L>): FFContext {
        eventDispatcher.notify(event)
        return this
    }
}
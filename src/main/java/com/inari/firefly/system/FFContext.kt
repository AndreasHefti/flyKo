package com.inari.firefly.system

import com.inari.commons.event.Event
import com.inari.commons.event.EventDispatcher
import com.inari.commons.event.IEventDispatcher
import com.inari.commons.event.Event.EventTypeKey
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.indexed.IndexedTypeKey
import com.inari.firefly.component.CompId
import com.inari.firefly.component.Component
import com.inari.firefly.component.IComponentMap


object FFContext {

    private val eventDispatcher: IEventDispatcher = EventDispatcher()
    private val componentMaps: MutableMap<IndexedTypeKey, IComponentMap<*>>  = HashMap()

    fun registerComponentMapper(mapper: IComponentMap<*>) {
        componentMaps.put(mapper.type().typeKey, mapper)
    }

    fun disposeComponentMapper(mapper: IComponentMap<*>) {
        componentMaps.remove(mapper.type().typeKey)
    }

    fun <C : Component> componentMapper(id: CompId): IComponentMap<C> =
            componentMapper(id.typeKey)

    @Suppress("UNCHECKED_CAST")
    fun <C : Component> componentMapper(key: IIndexedTypeKey): IComponentMap<C> {
        if (!componentMaps.containsKey(key)) {
            throw RuntimeException("No Component Mapper registered for type: $key")
        }
        return componentMaps[key] as IComponentMap<C>
    }

    fun <C : Component> get(id: CompId): C =
            componentMapper<C>(id).get(id.index)

    fun <C : Component> get(key: IIndexedTypeKey, name: String): C =
            componentMapper<C>(key).get(name)



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
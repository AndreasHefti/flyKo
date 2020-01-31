package com.inari.firefly

import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.component.*
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.FFAudio
import com.inari.firefly.external.FFGraphics
import com.inari.firefly.external.FFInput
import com.inari.firefly.external.FFTimer
import com.inari.firefly.system.component.*
import com.inari.util.aspect.Aspect
import com.inari.util.collection.DynArray
import com.inari.util.event.AspectedEvent
import com.inari.util.event.AspectedEventListener
import com.inari.util.event.Event
import com.inari.util.event.IEventDispatcher
import com.inari.util.indexed.Indexed


object FFContext {

    @PublishedApi @JvmField internal val componentMaps: DynArray<ComponentMap<*>> = DynArray.of()
    @JvmField internal val systemTypeMapping: DynArray<ComponentSystem> = DynArray.of()

    val eventDispatcher: IEventDispatcher
        get() = FFApp.eventDispatcher

    val graphics: FFGraphics
        get() = FFApp.graphics

    val audio: FFAudio
        get() = FFApp.audio

    val input: FFInput
        get() = FFApp.input

    val timer: FFTimer
        get() = FFApp.timer

    val screenWidth: Int
        get() = graphics.screenWidth
    val screenHeight: Int
        get() = graphics.screenHeight


    fun <S : ComponentSystem> loadSystem(system: S) {
        for (aspect in system.supportedComponents)
            systemTypeMapping[aspect.aspectIndex] = system
    }

    fun <C : Component> mapper(compAspect: Aspect): ComponentMap<C>  =
        @Suppress("UNCHECKED_CAST")
        if (SystemComponent.SYSTEM_COMPONENT_ASPECTS.typeCheck(compAspect) &&
            componentMaps.contains(compAspect.aspectIndex))

            componentMaps[compAspect.aspectIndex] as ComponentMap<C>
        else throw RuntimeException("No Component Mapper registered for compAspect: $compAspect")


    fun <C : Component> mapper(id: CompId): ComponentMap<C> =
        mapper<C>(id.componentType)

    fun <C : Component> mapper(id: CompNameId): ComponentMap<C> =
        mapper<C>(id.componentType)

    fun <C : SystemComponent> mapper(coType: SystemComponentType<C>): ComponentMap<C> =
        mapper(coType.compAspect)

    @Suppress("UNCHECKED_CAST")
    fun <C : Component> mapper(coType: ComponentType<C>): ComponentMap<C> {
        val index = coType.aspectIndex
        if (!componentMaps.contains(index))
            throw RuntimeException("No Component Mapper registered for subType: $coType")
        return componentMaps[index] as ComponentMap<C>
    }

    operator fun <C : Component> get(id: CompId): C =
        mapper<C>(id)[id.instanceId]

    operator fun <C : Component> get(id: CompNameId): C =
        mapper<C>(id)[id.name]

    operator fun <C : Component> get(cType: ComponentType<C>, index: Int): C =
        mapper(cType)[index]

    operator fun <C : Component> get(cType: ComponentType<C>, indexed: Indexed): C =
        mapper(cType)[indexed.index]

    operator fun <C : Component> get(cType: ComponentType<C>, name: String): C =
        mapper(cType)[name]

    operator fun <C : Component> get(cType: ComponentType<C>, named: Named): C =
        mapper(cType)[named.name]

    operator fun <C : SystemComponent, CC : C> get(cType: SystemComponentSubType<C, CC>, index: Int): CC =
        mapper<CC>(cType)[index]

    operator fun <C : SystemComponent, CC : C> get(cType: SystemComponentSubType<C, CC>, indexed: Indexed): CC =
        mapper<CC>(cType)[indexed.index]

    operator fun <C : SystemComponent, CC : C> get(cType: SystemComponentSubType<C, CC>, name: String): CC =
        mapper<CC>(cType)[name]

    operator fun <C : SystemComponent, CC : C> get(cType: SystemComponentSubType<C, CC>, named: Named): CC =
        mapper<CC>(cType)[named.name]

    fun assetInstanceId(assetId: Int): Int =
            AssetSystem.assets[assetId].instanceId

    fun assetInstanceId(assetName: String): Int =
            AssetSystem.assets[assetName].instanceId

    fun assetInstanceId(assetName: Named): Int =
        AssetSystem.assets[assetName.name].instanceId

    operator fun <E : EntityComponent> get(entityId: Int, ecType: EntityComponentType<E>): E =
        EntitySystem.entities[entityId][ecType]

    operator fun <E : EntityComponent> get(entityId: CompId, ecType: EntityComponentType<E>): E =
        EntitySystem.entities[entityId][ecType]

    operator fun <E : EntityComponent> get(entityName: String, ecType: EntityComponentType<E>): E =
        EntitySystem.entities[entityName][ecType]

    operator fun <E : EntityComponent> get(entityName: Named, ecType: EntityComponentType<E>): E =
        EntitySystem.entities[entityName.name][ecType]

    fun isActive(component: Component): Boolean =
        isActive(component.componentId)

    fun isActive(cType: ComponentType<*>, index: Int): Boolean =
        mapper<Component>(cType).isActive(index)

    fun isActive(cType: ComponentType<*>, indexed: Indexed): Boolean =
        mapper<Component>(cType).isActive(indexed.index)

    fun isActive(id: CompId): Boolean =
            mapper<Component>(id).isActive(id.instanceId)

    fun isActive(id: CompNameId): Boolean =
        mapper<Component>(id).isActive(id.name)

    fun isActive(cType: ComponentType<*>, name: String): Boolean =
        mapper<Component>(cType).isActive(name)

    fun isActive(cType: ComponentType<*>, named: Named): Boolean =
        mapper<Component>(cType).isActive(named.name)

    fun isActive(singleton: SingletonComponent<*, *>): Boolean =
        mapper<Component>(singleton).isActive(singleton.instance.index)

    fun activate(component: Component): FFContext {
        activate(component.componentId)
        return this
    }

    fun activate(cType: ComponentType<*>, index: Int): FFContext {
        mapper<Component>(cType).activate(index)
        return this
    }

    fun activate(cType: ComponentType<*>, indexed: Indexed): FFContext {
        mapper<Component>(cType).activate(indexed.index)
        return this
    }

    fun activate(id: CompId): FFContext {
        mapper<Component>(id).activate(id.instanceId)
        return this
    }

    fun activate(id: CompNameId): FFContext {
        mapper<Component>(id).activate(id.name)
        return this
    }

    fun activate(cType: ComponentType<*>, name: String): FFContext {
        mapper<Component>(cType).activate(name)
        return this
    }

    fun activate(cType: ComponentType<*>, named: Named): FFContext {
        mapper<Component>(cType).activate(named.name)
        return this
    }

    fun activate(singleton: SingletonComponent<*,*>): FFContext {
        mapper<Component>(singleton).activate(singleton.instance.index)
        return this
    }

    fun deactivate(component: Component): FFContext {
        deactivate(component.componentId)
        return this
    }

    fun deactivate(cType: ComponentType<*>, index: Int): FFContext {
        mapper<Component>(cType).deactivate(index)
        return this
    }

    fun deactivate(cType: ComponentType<*>, indexed: Indexed): FFContext {
        mapper<Component>(cType).deactivate(indexed.index)
        return this
    }

    fun deactivate(id: CompId): FFContext {
        mapper<Component>(id).deactivate(id.instanceId)
        return this
    }

    fun deactivate(id: CompNameId): FFContext {
        mapper<Component>(id).deactivate(id.name)
        return this
    }

    fun deactivate(cType: ComponentType<*>, name: String): FFContext {
        mapper<Component>(cType).deactivate(name)
        return this
    }

    fun deactivate(cType: ComponentType<*>, named: Named): FFContext {
        mapper<Component>(cType).deactivate(named.name)
        return this
    }

    fun deactivate(singleton: SingletonComponent<*,*>): FFContext {
        mapper<Component>(singleton).deactivate(singleton.instance.index)
        return this
    }

    fun delete(component: Component): FFContext {
        delete(component.componentId)
        return this
    }

    fun delete(cType: ComponentType<*>, index: Int): FFContext {
        mapper<Component>(cType).delete(index)
        return this
    }

    fun delete(cType: ComponentType<*>, indexed: Indexed): FFContext {
        mapper<Component>(cType).delete(indexed.index)
        return this
    }

    fun delete(id: CompId): FFContext {
        mapper<Component>(id).delete(id.instanceId)
        return this
    }

    fun delete(id: CompNameId): FFContext {
        mapper<Component>(id).delete(id.name)
        return this
    }

    fun delete(cType: ComponentType<*>, name: String): FFContext {
        mapper<Component>(cType).delete(name)
        return this
    }

    fun delete(cType: ComponentType<*>, named: Named): FFContext {
        mapper<Component>(cType).delete(named.name)
        return this
    }

    fun delete(singleton: SingletonComponent<*,*>): FFContext {
        singleton.dispose()
        return this
    }



    fun <L> registerListener(event: Event<L>, listener: L): FFContext {
        eventDispatcher.register(event.eventAspect, listener)
        return this
    }

    fun <L> registerListener(eventType: Aspect, listener: L): FFContext {
        eventDispatcher.register(eventType, listener)
        return this
    }

    fun <L> disposeListener(event: Event<L>, listener: L): FFContext {
        eventDispatcher.unregister(event.eventAspect, listener)
        return this
    }

    fun <L> disposeListener(eventType: Aspect, listener: L): FFContext {
        eventDispatcher.unregister(eventType, listener)
        return this
    }

    fun <L> notify(event: Event<L>): FFContext {
        eventDispatcher.notify(event)
        return this
    }

    fun <L : AspectedEventListener> notify(event: AspectedEvent<L>): FFContext {
        eventDispatcher.notify(event)
        return this
    }

}
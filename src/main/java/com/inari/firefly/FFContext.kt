package com.inari.firefly

import com.inari.commons.event.AspectedEventListener
import com.inari.commons.event.IEventDispatcher
import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.indexed.Indexed
import com.inari.commons.lang.list.DynArray
import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.component.*
import com.inari.firefly.control.task.TaskSystem
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponent.EntityComponentType
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.FFAudio
import com.inari.firefly.external.FFGraphics
import com.inari.firefly.external.FFInput
import com.inari.firefly.external.FFTimer
import com.inari.firefly.system.FFAspectedEvent
import com.inari.firefly.system.FFEvent
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.ISubType
import com.inari.firefly.system.component.SystemComponent


object FFContext {

    @JvmField internal val componentMaps: DynArray<ComponentMap<*>> =
            DynArray.create(ComponentMap::class.java)
    @JvmField internal val systemTypeMapping: DynArray<ComponentSystem> =
            DynArray.create(ComponentSystem::class.java)

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
        for (aspect in system.supportedComponents) {
            systemTypeMapping.set(aspect.index(), system)
        }
    }



    fun <C : Component> mapper(id: CompId): ComponentMap<C> =
        mapper(id.typeKey)

    fun <C : Component> mapper(id: CompNameId): ComponentMap<C> =
        mapper(id.typeKey)

    fun <C : Component> mapper(type: ComponentType<C>): ComponentMap<C> =
        mapper(type.typeKey)


    @Suppress("UNCHECKED_CAST")
    fun <C : Component> mapper(key: IIndexedTypeKey): ComponentMap<C> {
        val index = key.index()
        if (!componentMaps.contains(index)) {
            throw RuntimeException("No Component Mapper registered for type: $key")
        }
        return componentMaps[index] as ComponentMap<C>
    }

    operator fun <C : Component> get(id: CompId): C =
        mapper<C>(id)[id.index]

    operator fun <C : Component> get(id: CompNameId): C =
        mapper<C>(id)[id.name]

    operator fun <C : Component> get(cType: ComponentType<C>, index: Int): C =
        mapper(cType)[index]

    operator fun <C : Component> get(cType: ComponentType<C>, indexed: Indexed): C =
        mapper(cType)[indexed.index()]

    operator fun <C : Component> get(cType: ComponentType<C>, name: String): C =
        mapper(cType)[name]

    operator fun <C : Component> get(cType: ComponentType<C>, named: Named): C =
        mapper(cType)[named.name]

    @Suppress("UNCHECKED_CAST")
    operator fun <C : SystemComponent, CC : C> get(cType: ISubType<CC, C>, index: Int): CC =
        mapper(cType)[index] as CC

    @Suppress("UNCHECKED_CAST")
    operator fun <C : SystemComponent, CC : C> get(cType: ISubType<CC, C>, indexed: Indexed): CC =
        mapper(cType)[indexed.index()] as CC

    @Suppress("UNCHECKED_CAST")
    operator fun <C : SystemComponent, CC : C> get(cType: ISubType<CC, C>, name: String): CC =
        mapper(cType)[name] as CC

    @Suppress("UNCHECKED_CAST")
    operator fun <C : SystemComponent, CC : C> get(cType: ISubType<CC, C>, named: Named): CC =
        mapper(cType)[named.name] as CC

    fun assetInstanceId(assetId: Int): Int =
            AssetSystem.assets[assetId].instanceId()

    fun assetInstanceId(assetName: String): Int =
            AssetSystem.assets[assetName].instanceId()

    operator fun <E : EntityComponent> get(entityId: Int, ecType: EntityComponentType<E>): E =
        EntitySystem.entities[entityId][ecType]

    operator fun <E : EntityComponent> get(entityId: CompId, ecType: EntityComponentType<E>): E =
        EntitySystem.entities[entityId][ecType]

    operator fun <E : EntityComponent> get(entityName: String, ecType: EntityComponentType<E>): E =
        EntitySystem.entities[entityName][ecType]

    operator fun <E : EntityComponent> get(entityName: Named, ecType: EntityComponentType<E>): E =
        EntitySystem.entities[entityName.name][ecType]

    fun isActive(cType: ComponentType<*>, index: Int): Boolean =
        mapper<Component>(cType.typeKey).isActive(index)

    fun isActive(id: CompId): Boolean =
            mapper<Component>(id).isActive(id.index)

    fun isActive(id: CompNameId): Boolean =
        mapper<Component>(id).isActive(id.name)

    fun isActive(cType: ComponentType<*>, name: String): Boolean =
            mapper<Component>(cType.typeKey).isActive(name)

    fun activate(cType: ComponentType<*>, index: Int): FFContext {
        mapper<Component>(cType.typeKey).activate(index)
        return this
    }

    fun activate(id: CompId): FFContext {
        mapper<Component>(id).activate(id.index)
        return this
    }

    fun activate(id: CompNameId): FFContext {
        mapper<Component>(id).activate(id.name)
        return this
    }

    fun activate(cType: ComponentType<*>, name: String): FFContext {
        mapper<Component>(cType.typeKey).activate(name)
        return this
    }

    fun deactivate(cType: ComponentType<*>, index: Int): FFContext {
        mapper<Component>(cType.typeKey).deactivate(index)
        return this
    }

    fun deactivate(id: CompId): FFContext {
        mapper<Component>(id).deactivate(id.index)
        return this
    }

    fun deactivate(id: CompNameId): FFContext {
        mapper<Component>(id).deactivate(id.name)
        return this
    }

    fun deactivate(cType: ComponentType<*>, name: String): FFContext {
        mapper<Component>(cType.typeKey).deactivate(name)
        return this
    }

    fun delete(cType: ComponentType<*>, index: Int): FFContext {
        mapper<Component>(cType.typeKey).delete(index)
        return this
    }

    fun delete(id: CompId): FFContext {
        mapper<Component>(id).delete(id.index)
        return this
    }

    fun delete(id: CompNameId): FFContext {
        mapper<Component>(id).delete(id.name)
        return this
    }

    fun delete(cType: ComponentType<*>, name: String): FFContext {
        mapper<Component>(cType.typeKey).delete(name)
        return this
    }



    fun <L> registerListener(eventType: FFEvent<*>, listener: L): FFContext {
        eventDispatcher.register(eventType.typeKey, listener)
        return this
    }

    fun <L> disposeListener(eventType: FFEvent<*>, listener: L): FFContext {
        eventDispatcher.unregister(eventType.typeKey, listener)
        return this
    }

    fun <L> registerListener(eventType: FFAspectedEvent<*>, listener: L): FFContext {
        eventDispatcher.register(eventType.typeKey, listener)
        return this
    }

    fun <L> disposeListener(eventType: FFAspectedEvent<*>, listener: L): FFContext {
        eventDispatcher.unregister(eventType.typeKey, listener)
        return this
    }

    fun <L> notify(event: FFEvent<L>): FFContext {
        eventDispatcher.notify(event)
        return this
    }

    fun <L : AspectedEventListener> notify(event: FFAspectedEvent<L>): FFContext {
        eventDispatcher.notify(event)
        return this
    }

}
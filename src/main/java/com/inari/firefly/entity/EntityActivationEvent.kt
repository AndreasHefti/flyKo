package com.inari.firefly.entity

import com.inari.firefly.FFContext
import com.inari.firefly.system.FFEvent

typealias EntityActivationEventListener = (Entity, EntityActivationEvent.Type) -> Unit
object EntityActivationEvent : FFEvent<EntityActivationEventListener>(createTypeKey(EntityActivationEvent::class.java)) {

    enum class Type {
        ACTIVATED,
        DEACTIVATED
    }

    private lateinit var entity: Entity
    private lateinit var type: EntityActivationEvent.Type

    override fun notify(listener: EntityActivationEventListener) =
        listener(entity, type)

    fun send(entity: Entity, type: EntityActivationEvent.Type) {
        this.entity = entity
        this.type = type
        FFContext.notify(this)
    }

}
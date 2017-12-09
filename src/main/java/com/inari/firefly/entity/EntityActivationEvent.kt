package com.inari.firefly.entity

import com.inari.commons.event.AspectedEventListener
import com.inari.firefly.FFContext
import com.inari.firefly.system.FFEvent

object EntityActivationEvent : FFEvent<EntityActivationEvent.Listener>(createTypeKey(EntityActivationEvent::class.java)) {

    enum class Type {
        ACTIVATED,
        DEACTIVATED
    }

    private lateinit var entity: Entity
    private lateinit var type: EntityActivationEvent.Type

    override fun notify(listener: EntityActivationEvent.Listener) =
        when(type) {
            Type.ACTIVATED -> listener.entityActivated(entity)
            Type.DEACTIVATED -> listener.entityDeactivated(entity)
        }

    fun send(entity: Entity, type: EntityActivationEvent.Type) {
        this.entity = entity
        this.type = type
        FFContext.notify(this)
    }

    interface Listener : AspectedEventListener {
        fun entityActivated(entity: Entity)
        fun entityDeactivated(entity: Entity)
    }
}
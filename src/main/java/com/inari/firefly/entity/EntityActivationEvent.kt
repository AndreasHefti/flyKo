package com.inari.firefly.entity

import com.inari.firefly.FFContext

import com.inari.util.aspect.Aspects
import com.inari.util.event.AspectedEvent
import com.inari.util.event.AspectedEventListener
import com.inari.util.indexed.Indexer

object EntityActivationEvent : AspectedEvent<EntityActivationEvent.Listener>() {

    enum class Type {
        ACTIVATED,
        DEACTIVATED
    }



    private lateinit var entity: Entity
    private lateinit var type: EntityActivationEvent.Type

    override val aspects: Aspects get() =
        entity.aspects

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
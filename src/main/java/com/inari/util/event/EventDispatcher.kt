/*******************************************************************************
 * Copyright (c) 2015 - 2016 - 2016, Andreas Hefti, inarisoft@yahoo.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inari.util.event

import com.inari.commons.lang.list.DynArray
import com.inari.commons.lang.list.StaticList
import com.inari.util.indexed.Indexed

/** A simple, synchronous and none thread save implementation of the [IEventDispatcher] interface.
 *
 * @author andreas hefti
 * @see IEventDispatcher
 */
class EventDispatcher : IEventDispatcher {

    private val eventLog: IEventLog?

    private val listeners = DynArray.createTyped<DynArray<StaticList<*>>>(StaticList::class.java, 10, 10)

    constructor() {
        eventLog = null
    }

    constructor(eventLog: IEventLog) {
        this.eventLog = eventLog
    }

    override fun <L> register(eventType: Event.EventTypeKey, listener: L) {
        val listenersOfType = getListenersOfType<L>(eventType, true)
        if (!listenersOfType!!.contains(listener))
            listenersOfType.add(listener)
    }

    override fun <L> unregister(eventType: Event.EventTypeKey, listener: L): Boolean {
        val listenersOfType = getListenersOfType<L>(eventType, false)
        return listenersOfType != null && listenersOfType.remove(listener) >= 0
    }

    override fun <L> notify(event: Event<L>) {
        eventLog?.log(event)
        val listenersOfType = this.getListenersOfType<L>(event, false)
        if (listenersOfType != null)
            for (i in 0 until listenersOfType.capacity()) {
                val listener = listenersOfType.get(i) ?: continue
                event._notify(listener)
            }

        event._restore()
    }

    override fun <L : AspectedEventListener> notify(event: AspectedEvent<L>) {
        eventLog?.log(event)
        val listenersOfType = this.getListenersOfType<L>(event.indexedTypeKey, false)
        if (listenersOfType != null)
            for (i in 0 until listenersOfType.capacity()) {
                val listener = listenersOfType.get(i)
                if (listener != null && listener.match(event.aspects))
                    event._notify(listener)
            }

        event._restore()
    }

    override fun <L : PredicatedEventListener> notify(event: PredicatedEvent<L>) {
        eventLog?.log(event)
        val listenersOfType = this.getListenersOfType<L>(event, false)
        if (listenersOfType != null)
            for (i in 0 until listenersOfType.capacity()) {
                val listener = listenersOfType.get(i) ?: continue
                val matcher = listener.getMatcher<L>()
                if (matcher(event))
                    event._notify(listener)
            }

        event._restore()
    }

    override fun toString(): String =
        "EventDispatcher [listeners=$listeners]"

    @Suppress("UNCHECKED_CAST")
    private fun <L> getListenersOfType(indexed: Indexed, create: Boolean): StaticList<L>? {
        val eventIndex = indexed.index
        var listenersOfType: StaticList<L>? = null
        if (listeners.contains(eventIndex))
            listenersOfType = listeners.get(eventIndex) as StaticList<L>
        else
            if (create) {
                listenersOfType = StaticList(10, 10)
                listeners.set(eventIndex, listenersOfType)
            }

        return listenersOfType
    }
}

package com.inari.firefly.control.state

import com.inari.commons.lang.list.DynArray
import com.inari.firefly.Condition
import com.inari.firefly.FALSE_CONDITION
import com.inari.firefly.NO_STATE
import com.inari.firefly.component.ArrayPropertyAccessor
import com.inari.firefly.system.component.SingleType
import com.inari.firefly.system.component.SystemComponent


class Workflow private constructor() : SystemComponent() {

    @JvmField internal var startState: String = NO_STATE
    @JvmField internal val states = DynArray.create(String::class.java)
    @JvmField internal val stateChanges = DynArray.create(StateChange::class.java)

    var ff_StartState
        get() = startState
        set(value) {startState = value}
    val ff_States = ArrayPropertyAccessor(states)
    val ff_StateChanges = ArrayPropertyAccessor(stateChanges)

    var currentState: String = NO_STATE
        internal set(value) {
            field = value
            currentStateChanges.clear()
            stateChanges.forEach({ st ->
                if (st.from == currentState) {
                    currentStateChanges.add(st)
                }
            })
        }

    internal val currentStateChanges: DynArray<StateChange> =
        DynArray.create(StateChange::class.java)

    override fun indexedTypeKey() = typeKey

    fun findStateChangeForTargetState(targetStateName: String): StateChange? =
        stateChanges.firstOrNull {
            it.from == currentState && targetStateName == it.to
        }


    fun findStateChangeForCurrentState(stateChangeName: String): StateChange? =
        stateChanges.firstOrNull {
            stateChangeName == it.name && it.from == currentState
        }

    fun reset() {
        currentState = ff_StartState
    }

    companion object : SingleType<Workflow>() {
        override val typeKey = SystemComponent.createTypeKey(Workflow::class.java)
        override fun createEmpty() = Workflow()
    }

    data class StateChange(
        val name: String,
        val from: String,
        val to: String,
        val condition: Condition = FALSE_CONDITION
    )

}
package com.inari.firefly.control.state

import com.inari.firefly.FALSE_SUPPLIER
import com.inari.firefly.NO_STATE
import com.inari.firefly.component.ArrayAccessor
import com.inari.firefly.component.ComponentType
import com.inari.firefly.system.component.SystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType
import com.inari.util.BooleanSupplier
import com.inari.util.collection.DynArray


class Workflow private constructor() : SystemComponent(Workflow::class.java.name) {

    @JvmField internal var startState: String = NO_STATE
    @JvmField internal val states: DynArray<String> = DynArray.of()
    @JvmField internal val stateChanges: DynArray<StateChange> = DynArray.of()

    @JvmField internal val currentStateChanges: DynArray<StateChange> = DynArray.of()

    var ff_StartState
        get() = startState
        set(value) {startState = value}
    val ff_States = ArrayAccessor(states)
    val ff_StateChanges = ArrayAccessor(stateChanges)

    var currentState: String = NO_STATE
        internal set(value) {
            field = value
            currentStateChanges.clear()
            stateChanges.forEach { st ->
                if (st.from == currentState)
                    currentStateChanges.add(st)
            }
        }

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

    override fun componentType(): ComponentType<Workflow> = Companion
    companion object : SystemComponentSingleType<Workflow>(Workflow::class.java) {
        override fun createEmpty() = Workflow()
    }

    data class StateChange(
        val name: String,
        val from: String,
        val to: String,
        val condition: BooleanSupplier = FALSE_SUPPLIER
    )

}
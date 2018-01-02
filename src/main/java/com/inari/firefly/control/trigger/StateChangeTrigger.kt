package com.inari.firefly.control.trigger

import com.inari.firefly.Call
import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.NULL_CALL
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.state.Workflow
import com.inari.firefly.control.state.WorkflowEvent

class StateChangeTrigger private constructor(): Trigger() {

    enum class Type {
        STATE_CHANGE,
        ENTER_STATE,
        EXIT_STATE
    }

    private var workflowRef = -1
    private var typeName = NO_NAME
    private var triggerType = Type.STATE_CHANGE
    private var call: Call = NULL_CALL
    private val listener = object : WorkflowEvent.Listener {

        override fun invoke(type: WorkflowEvent.Type, workflow: CompId, workflowName: String, stateChangeName: String, fromName: String, toName: String) {
            if (workflow.index != workflowRef)
                return

            when (triggerType) {
                Type.STATE_CHANGE ->
                    if (type === WorkflowEvent.Type.STATE_CHANGED && typeName == stateChangeName)
                        doTrigger(call)
                Type.ENTER_STATE ->
                    if ((type === WorkflowEvent.Type.STATE_CHANGED ||
                        type === WorkflowEvent.Type.WORKFLOW_STARTED) &&
                        typeName == toName)
                        doTrigger(call)
                Type.EXIT_STATE ->
                    if ((type === WorkflowEvent.Type.STATE_CHANGED ||
                        type === WorkflowEvent.Type.WORKFLOW_FINISHED) &&
                        typeName == fromName)
                        doTrigger(call)
            }
        }
    }

    var ff_Type : Type
        get() = triggerType
        set(value) {triggerType = value}
    val ff_Workflow = ComponentRefResolver(Workflow, { index-> workflowRef = index })
    var ff_TypeName : String
        get() = typeName
        set(value) {typeName = value}

    override fun register(call: Call) {
        this.call = call
        FFContext.registerListener(WorkflowEvent, listener)
    }

    override fun dispose() {
        FFContext.disposeListener(WorkflowEvent, listener)
        call = NULL_CALL
        super.dispose()
    }

    companion object : Trigger.Subtype<StateChangeTrigger>() {
        override fun createEmpty() = StateChangeTrigger()
    }
}
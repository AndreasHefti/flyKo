package com.inari.firefly.control.trigger

import com.inari.firefly.FFContext
import com.inari.firefly.NO_NAME
import com.inari.firefly.NULL_CALL
import com.inari.firefly.component.CompId
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.state.Workflow
import com.inari.firefly.control.state.WorkflowEvent
import com.inari.util.Call

class StateChangeTrigger private constructor(): Trigger() {

    enum class Type {
        STATE_CHANGE,
        ENTER_STATE,
        EXIT_STATE
    }

    private var workflowRef = -1
    private var call: Call = NULL_CALL
    private val listener = object : WorkflowEvent.Listener {

        override fun invoke(type: WorkflowEvent.Type, workflowId: CompId, workflowName: String, stateChangeName: String, fromName: String, toName: String) {
            if (workflowId.instanceId != workflowRef)
                return

            when (this@StateChangeTrigger.type) {
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

    var type : Type = Type.STATE_CHANGE
    val workflow = ComponentRefResolver(Workflow) { index -> workflowRef = index }
    var typeName : String  = NO_NAME


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
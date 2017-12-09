package com.inari.firefly.control.state

import com.inari.firefly.FFContext
import com.inari.firefly.component.CompId
import com.inari.firefly.system.FFEvent

object WorkflowEvent : FFEvent<WorkflowEvent.Listener>(createTypeKey(WorkflowEvent::class.java)) {

    enum class Type {
        WORKFLOW_STARTED,
        STATE_CHANGED,
        WORKFLOW_FINISHED
    }

    private lateinit var type: WorkflowEvent.Type
    private lateinit var workflow: CompId
    private lateinit var stateChange : Workflow.StateChange

    override fun notify(listener: WorkflowEvent.Listener) =
        listener(type, workflow, stateChange)

    fun send(
        type: WorkflowEvent.Type,
        workflow: CompId,
        stateChange : Workflow.StateChange = null!!
    ) {
        this.type = type
        this.workflow = workflow
        this.stateChange = stateChange
        FFContext.notify(this)
    }

    interface Listener {
        operator fun invoke(
            type: WorkflowEvent.Type,
            workflow: CompId,
            stateChange: Workflow.StateChange
        )
    }
}
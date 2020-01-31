package com.inari.firefly.control.state

import com.inari.firefly.FFContext
import com.inari.firefly.NO_COMP_ID
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.CompId
import com.inari.util.event.Event

object WorkflowEvent : Event<WorkflowEvent.Listener>(EVENT_ASPECTS.createAspect(WorkflowEvent::class.java.simpleName)) {

    enum class Type {
        WORKFLOW_STARTED,
        STATE_CHANGED,
        WORKFLOW_FINISHED
    }

    private lateinit var type: Type
    private var workflow: CompId = NO_COMP_ID
    private var workflowName = NO_NAME
    private var stateChangeName = NO_NAME
    private var fromName = NO_NAME
    private var toName = NO_NAME

    override fun notify(listener: Listener) =
        listener(type, workflow, workflowName, stateChangeName, fromName, toName)

    fun send(
        type: Type,
        workflowId: CompId,
        stateChangeName: String = NO_NAME,
        fromName: String = NO_NAME,
        toName: String = NO_NAME
    ) {
        this.type = type
        this.workflow = workflowId
        workflowName = StateSystem.workflows[workflowId].name
        this.stateChangeName = stateChangeName
        this.fromName = fromName
        this.toName = toName
        FFContext.notify(this)
    }

    fun send(
        type: Type,
        workflowId: CompId,
        stateChange: Workflow.StateChange
    ) {
        this.type = type
        this.workflow = workflowId
        workflowName = StateSystem.workflows[workflowId].name
        this.stateChangeName = stateChange.name
        this.fromName = stateChange.from
        this.toName = stateChange.to
        FFContext.notify(this)
    }

    interface Listener {
        operator fun invoke(
            type: Type,
            workflowId: CompId,
            workflowName: String,
            stateChangeName: String,
            fromName: String,
            toName: String
        )
    }
}
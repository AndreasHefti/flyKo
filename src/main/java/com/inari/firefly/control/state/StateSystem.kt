package com.inari.firefly.control.state

import com.inari.commons.lang.aspect.IAspects
import com.inari.firefly.Call
import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.NO_STATE
import com.inari.firefly.component.ComponentMap
import com.inari.firefly.external.FFTimer
import com.inari.firefly.system.component.ComponentSystem
import com.inari.firefly.system.component.SystemComponent.Companion.ASPECT_GROUP
import com.inari.firefly.control.state.Workflow.StateChange



object StateSystem : ComponentSystem {
    override val supportedComponents: IAspects =
        ASPECT_GROUP.createAspects(Workflow)

    @JvmField val workflows = ComponentSystem.createComponentMapping(
        Workflow,
        activationMapping = true,
        nameMapping = true,
        listener = { workflow, action -> when (action) {
            ComponentMap.MapAction.ACTIVATED     -> activated(workflow)
            ComponentMap.MapAction.DEACTIVATED   -> deactivated(workflow)
            else -> {}
        } }
    )

    private val scheduler: FFTimer.UpdateScheduler =
        FFContext.timer.createUpdateScheduler(10f)

    init {
        FFContext.registerListener(
            FFApp.UpdateEvent,
            object : FFApp.UpdateEvent.Listener {
                override fun invoke() {
                    if (scheduler.needsUpdate())
                        update()
                }
            }
        )

        FFContext.loadSystem(this)
    }

    operator fun get(workflowIndex: Int): String =
        workflows[workflowIndex].currentState

    operator fun get(workflowName: String): String =
        workflows[workflowName].currentState

    private fun update() {
        var j = 0
        while (j < workflows.map.capacity()) {
            val workflow = workflows.map[j++] ?: continue
//            doStateChange(workflow, workflow.currentStateChanges.first {
//                st -> st.condition()
//            } )
            var i = 0
            while (i < workflow.currentStateChanges.capacity()) {
                val st = workflow.currentStateChanges[i++] ?: continue
                if (st.condition()) {
                    doStateChange(workflow, st)
                    break
                }
            }
        }
    }

    fun doStateChange(workflowId: Int, stateChangeName: String) {
        val workflow = workflows[workflowId]
        doStateChange(workflow, workflow.findStateChangeForCurrentState(stateChangeName)!!)
    }

    fun changeState(workflowId: Int, targetStateName: String) {
        val workflow = workflows[workflowId]
        doStateChange(workflow, workflow.findStateChangeForTargetState(targetStateName)!!)
    }

    fun createChangeToStateCall(workflowId: Int, targetStateName: String): Call = {
        changeState(workflowId, targetStateName)
    }

    fun createStateChangeCall(workflowId: Int, stateChangeName: String): Call = {
        changeState(workflowId, stateChangeName)
    }

    private fun doStateChange(workflow: Workflow, stateChange: StateChange) {
        workflow.currentState = stateChange.to

        if (stateChange.to !== NO_STATE) {
            WorkflowEvent.send(
                WorkflowEvent.Type.STATE_CHANGED,
                workflow.componentId,
                stateChange
            )
        } else {
            WorkflowEvent.send(
                WorkflowEvent.Type.WORKFLOW_FINISHED,
                workflow.componentId,
                stateChange
            )
        }
    }

    private fun activated(workflow: Workflow) {
        WorkflowEvent.send(
            WorkflowEvent.Type.WORKFLOW_STARTED,
            workflow.componentId
        )
    }

    private fun deactivated(workflow: Workflow) {
        workflow.reset()
        WorkflowEvent.send(
            WorkflowEvent.Type.WORKFLOW_FINISHED,
            workflow.componentId
        )
    }

    override fun clearSystem() {
        workflows.clear()
    }
}
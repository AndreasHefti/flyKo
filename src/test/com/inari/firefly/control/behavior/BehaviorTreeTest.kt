package com.inari.firefly.control.behavior

import com.inari.firefly.TestApp
import com.inari.firefly.control.behavior.BehaviorSystem.FALSE_CONDITION
import com.inari.firefly.control.task.EntityTask
import com.inari.firefly.control.task.TaskSystem
import com.inari.util.IntOperation
import com.inari.util.OpResult
import org.junit.Before
import org.junit.Test

class BehaviorTreeTest {

    @Before
    fun init() {
        TestApp
        BehaviorSystem.clearSystem()
        TaskSystem.clearSystem()
    }

    @Test
    fun testCreation() {

        EntityTask.build {
            name = "Task_Name"
            task = object : IntOperation {
                override fun invoke(index: Int): OpResult {
                    return OpResult.SUCCESS
                }
            }
        }

        BxSelection.build {
            name = "First Selection"
            node(BxSelection) {
                name = "Second Selection"
                node(BxCondition) {
                    name ="Condition 1"
                    condition = FALSE_CONDITION
                }
                node(BxAction) {
                    name = "First Task"
                    tickOp = { entity, _ -> TaskSystem.runEntityTask("Task_Name", entity.index) }
                }
                node(BxSequence) {
                    name = ""
                }

            }
            node(BxSequence) {

            }
        }
    }
}
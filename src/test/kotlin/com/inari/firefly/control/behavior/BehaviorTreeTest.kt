package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.TestApp
import com.inari.firefly.control.behavior.BehaviorSystem.FALSE_CONDITION
import com.inari.firefly.control.task.EntityTask
import com.inari.firefly.control.task.TaskSystem
import com.inari.util.IntOperation
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
            ff_Name = "Task_Name"
            ff_Task = object : IntOperation {
                override fun invoke(index: Int): OpResult {
                    return OpResult.SUCCESS
                }
            }
        }

        BxSelection.build {
            ff_Name = "First Selection"
            ff_WithNode(BxSelection) {
                ff_Name = "Second Selection"
                ff_WithNode(BxCondition) {
                    ff_Name ="Condition 1"
                    ff_Condition = FALSE_CONDITION
                }
                ff_WithNode(BxAction) {
                    ff_Name = "First Task"
                    ff_TickOp = { entity, _ -> TaskSystem.runEntityTask("Task_Name", entity.index) }
                }
                ff_WithNode(BxSequence) {
                    ff_Name = ""
                }

            }
            ff_WithNode(BxSequence) {

            }
        }
    }
}
package com.inari.firefly.control.behavior

import com.inari.firefly.IntOperation
import com.inari.firefly.OpResult
import com.inari.firefly.TestApp
import com.inari.firefly.asset.AssetSystem
import com.inari.firefly.composite.CompositeSystem
import com.inari.firefly.control.task.EntityTask
import com.inari.firefly.control.task.TaskSystem
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.firefly.physics.animation.AnimationSystem
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

        BehaviorTree.build {
            ff_Name = "Test Tree"
            ff_WithRootNode(Selection) {
                ff_Name = "First Selection"
                ff_WithNode(Selection) {
                    ff_Name = "Second Selection"
                    ff_WithNode(Condition) {
                        ff_Name ="Condition 1"
                        ff_Condition = { _, _ -> false }
                    }
                    ff_WithNode(BehaviorTask) {
                        ff_Name = "First Task"
                        ff_Task("Task_Name")
                    }

                }
            }
        }
    }
}
package com.inari.firefly.libgdx

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.IntOperation
import com.inari.firefly.IntPredicate
import com.inari.firefly.OpResult
import com.inari.firefly.control.behavior.*
import com.inari.firefly.control.task.EntityTask
import com.inari.firefly.control.task.TaskSystem
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.physics.movement.EMovement
import com.inari.firefly.physics.movement.MovementSystem
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo

class BehaviorTest : GDXAppAdapter() {

    override val title: String = "BehaviorTest"

    override fun init() {
        FFInfoSystem
                .addInfo(FrameRateInfo)
                .activate()
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(BehaviorSystem)
        FFContext.loadSystem(TaskSystem)
        MovementSystem



        EntityTask.build {
            ff_Name = "GoLeft"
            ff_Task = object : IntOperation {
                override fun invoke(index: Int): OpResult {
                    EntitySystem[index][EMovement].ff_VelocityX = -1f
                    return OpResult.RUNNING
                }
            }
        }
        EntityTask.build {
            ff_Name = "GoRight"
            ff_Task = object : IntOperation {
                override fun invoke(index: Int): OpResult {
                    EntitySystem[index][EMovement].ff_VelocityX = 1f
                    return OpResult.RUNNING
                }
            }
        }

        val test: (Int?) -> Boolean? = {
            i -> i!! > 100
        }

        BehaviorTree.buildAndActivate {
            ff_Name = "Behavior 1"
            ff_WithRootNode(BxSelection) {
                ff_Name = "Root Selection"
                ff_WithNode(BxSequence) {
                    ff_Name = "right"
                    ff_WithNode(BxCondition) {
                        ff_Condition = object : IntPredicate {
                            override fun invoke(i: Int): Boolean =
                                EntitySystem[i][ETransform].ff_Position.x > 200
                        }
                    }
                    ff_WithNode(BxTask) {
                        ff_Task("GoLeft")
                    }
                }
                ff_WithNode(BxSequence) {
                    ff_Name = "left"
                    ff_WithNode(BxCondition) {
                        ff_Condition = object : IntPredicate {
                            override fun invoke(i: Int): Boolean =
                                    EntitySystem[i][ETransform].ff_Position.x < 50
                        }
                    }
                    ff_WithNode(BxTask) {
                        ff_Task("GoRight")
                    }
                }
            }
        }

        Entity.buildAndActivate {
            withComponent(ETransform) {
                ff_View(0)
                ff_Position(50, 50)
            }
            withComponent(EShape) {
                ff_Type = ShapeType.RECTANGLE
                ff_Fill = true
                ff_Color(1f, 0f, 0f, 1f)
                ff_Vertices = floatArrayOf(0f, 0f, 20f, 20f)
            }
            withComponent(EMovement) {
                ff_Active = true
                ff_VelocityX = 1f
            }
            withComponent(EBehavior) {
                ff_Active = true
                ff_BehaviorTree("Behavior 1")
                ff_Repeat = true
            }
        }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            try {
                val config = LwjglApplicationConfiguration()
                config.resizable = true
                config.width = 800
                config.height = 600
                LwjglApplication(BehaviorTest(), config)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}
package com.inari.firefly.control.behavior

import com.inari.firefly.OpResult
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.behavior.BehaviorSystem.BEHAVIOR_STATE_ASPECT_GROUP
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import com.inari.util.aspect.Aspects
import java.util.*

class EBehavior private constructor() : EntityComponent(EBehavior::class.java.name){

    @JvmField internal var treeRef = -1
    @JvmField internal var repeat = true
    @JvmField internal var active = true
    @JvmField internal var treeState = OpResult.SUCCESS

    @JvmField internal var actionsDone: Aspects = BEHAVIOR_STATE_ASPECT_GROUP.createAspects()

    val ff_BehaviorTree = ComponentRefResolver(BxNode) { index-> treeRef = index }

    var ff_Repeat: Boolean
        get() = repeat
        set(value) { repeat = value }

    var ff_Active: Boolean
        get() = active
        set(value) { active = value }

    val ff_TreeState: OpResult
        get() = treeState


    override fun reset() {
        treeRef = -1
        repeat = true
        treeState = OpResult.SUCCESS
        actionsDone.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EBehavior>(EBehavior::class.java) {
        override fun createEmpty() = EBehavior()
    }
}
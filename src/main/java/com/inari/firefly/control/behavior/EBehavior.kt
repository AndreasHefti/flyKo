package com.inari.firefly.control.behavior

import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.behavior.BehaviorTree.Status
import com.inari.firefly.entity.EntityComponent
import com.inari.firefly.entity.EntityComponentType
import java.util.*

class EBehavior private constructor() : EntityComponent(EBehavior::class.java.name){

    @JvmField internal var treeRef = -1
    @JvmField internal var repeat = true
    @JvmField internal var active = true
    @JvmField internal var status = Status.RESET
    @JvmField internal var runningTasks = BitSet()

    val ff_BehaviorTree = ComponentRefResolver(BehaviorTree) { index-> treeRef = index }
    var ff_Repeat: Boolean
        get() = repeat
        set(value) {
            repeat = value}
    var ff_Active: Boolean
        get() = active
        set(value) {
            active = value}
    val ff_CurrentStatus: Status
        get() = status


    override fun reset() {
        treeRef = -1
        repeat = true
        status = Status.RESET
        runningTasks.clear()
    }

    override fun componentType() = Companion
    companion object : EntityComponentType<EBehavior>(EBehavior::class.java) {
        override fun createEmpty() = EBehavior()
    }
}
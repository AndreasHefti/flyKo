package com.inari.firefly.entity

import com.inari.commons.lang.indexed.IIndexedTypeKey
import com.inari.commons.lang.list.IntBag
import com.inari.firefly.NO_NAME
import com.inari.firefly.component.NamedComponent

class EMeta private constructor (
    var ff_Name: String = NO_NAME,
    var ff_Controller: IntBag,
    var ff_Parent: Int,
    var ff_ZPos: Int
) : EntityComponent(), NamedComponent {


    private constructor() : this(NO_NAME, IntBag(5, -1, 5), -1, -1)

    override fun indexedTypeKey(): IIndexedTypeKey = typeKey
    override fun name(): String = ff_Name
    override fun reset() {
        this.also {
            ff_Name = NO_NAME
            ff_Controller.clear()
            ff_Parent = -1
            ff_ZPos = -1
        }
    }

    override fun toString(): String =
        "EMeta(ff_Name=$ff_Name, " +
        "ff_Controller=$ff_Controller, " +
        "ff_Parent=$ff_Parent, " +
        "ff_ZPos=$ff_ZPos)"


    companion object : EntityComponentType<EMeta>() {
        override val typeKey = EntityComponent.createTypeKey(EMeta::class.java)
        override fun createEmpty(): EMeta = EMeta()
    }
}
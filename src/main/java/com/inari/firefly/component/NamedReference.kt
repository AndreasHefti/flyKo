package com.inari.firefly.component

import com.inari.firefly.NO_NAME
import com.inari.firefly.FFContext

class NamedReference<C : Component> (
    val type: ComponentType<C>
) {

    var index: Int = -1
        get() {
            if (field < 0 && name != NO_NAME) {
                field = FFContext.mapper(type).indexForName(name)
            }
            return field
        }

    var name: String = NO_NAME
        set(n) {
            if (n != NO_NAME) {
                // if we have a new name reset the index to get
                // resolved again when next time called
                index = -1
            }
            field = n
        }

    fun defined(): Boolean = index >= 0 || name != NO_NAME

    override fun toString(): String {
        return "NamedReference(index=$index, name='$name')"
    }

}
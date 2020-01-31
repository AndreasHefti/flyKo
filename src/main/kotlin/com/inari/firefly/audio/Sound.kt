package com.inari.firefly.audio

import com.inari.firefly.FFContext
import com.inari.firefly.component.ComponentRefResolver
import com.inari.firefly.control.Controller
import com.inari.firefly.control.trigger.Trigger
import com.inari.firefly.control.trigger.TriggeredSystemComponent
import com.inari.firefly.system.component.SystemComponentSingleType

class Sound private constructor() : TriggeredSystemComponent(Sound::class.java.name) {

    @JvmField internal var soundAssetId: Int = -1
    @JvmField internal var controllerId: Int = -1
    @JvmField internal var looping: Boolean = false
    @JvmField internal var volume: Float = 1.0f
    @JvmField internal var pitch: Float = 1.0f
    @JvmField internal var pan: Float = 0.0f
    @JvmField internal var channel: Int = 0
    @JvmField internal var playId: Long = -1

    val ff_SoundAsset = ComponentRefResolver(SoundAsset) { index -> soundAssetId = index }
    val ff_Controller = ComponentRefResolver(Controller) { index -> controllerId = index }
    var ff_Looping: Boolean
        get() = looping
        set(value) { looping = value }
    var ff_Volume: Float
        get() = volume
        set(value) { volume = value }
    var ff_Pitch: Float
        get() = pitch
        set(value) { pitch = value }
    var ff_Pan: Float
        get() = pan
        set(value) { pan = value }
    var ff_Channel: Int
        get() = channel
        set(value) { channel = value }

    fun <A : Trigger> withPlayTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, { FFContext.activate(this) }, configure)

    fun <A : Trigger> withStopTrigger(cBuilder: Trigger.Subtype<A>, configure: (A.() -> Unit)): A =
        super.with(cBuilder, { FFContext.deactivate(this) }, configure)

    override fun componentType() = Companion
    companion object : SystemComponentSingleType<Sound>(Sound::class.java) {
        override fun createEmpty() = Sound()
    }

}
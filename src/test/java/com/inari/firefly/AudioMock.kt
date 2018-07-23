package com.inari.firefly

import com.inari.firefly.external.FFAudio
import java.util.*

object AudioMock : FFAudio {

    private val loaded = ArrayList<String>()
    private val log = ArrayList<String>()


    fun clear() {
        loaded.clear()
        log.clear()
    }

    override fun playSound(soundId: Int, channel: Int, looping: Boolean, volume: Float, pitch: Float, pan: Float): Long {
        log.add("playSound")
        return soundId.toLong()
    }

    override fun changeSound(soundId: Int, instanceId: Long, volume: Float, pitch: Float, pan: Float) {
        log.add("changeSound")
    }

    override fun stopSound(soundId: Int, instanceId: Long) {
        log.add("stopSound")
    }

    override fun playMusic(soundId: Int, looping: Boolean, volume: Float, pan: Float) {
        log.add("playMusic")
    }

    override fun changeMusic(soundId: Int, volume: Float, pan: Float) {
        log.add("changeMusic")
    }

    override fun stopMusic(soundId: Int) {
        log.add("stopMusic")
    }

    override fun createSound(resourceName: String, streaming: Boolean): Int {
        loaded.add(resourceName)
        return loaded.indexOf(resourceName)
    }

    override fun disposeSound(soundId: Int) {
        loaded.remove(loaded[soundId])
    }
}

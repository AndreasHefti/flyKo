package com.inari.firefly.libgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.inari.firefly.external.FFAudio
import com.inari.util.collection.DynArray
import com.inari.util.collection.DynIntArray


object GDXAudio : FFAudio {

    private val sounds: DynArray<Sound> = DynArray.of()
    private val lastPlayingSoundOnChannel = DynIntArray(5, -1)
    private val music: DynArray<Music> = DynArray.of()

    override fun createSound(resourceName: String, streaming: Boolean): Int =
        if (streaming)
            music.add(Gdx.audio.newMusic(Gdx.files.classpath(resourceName)))
        else
            sounds.add(Gdx.audio.newSound(Gdx.files.classpath(resourceName)))

    override fun disposeSound(soundId: Int, streaming: Boolean) {
        if (streaming)
            music.remove(soundId)?.dispose()
        else
            sounds.remove(soundId)?.dispose()
    }

    override fun changeMusic(soundId: Int, volume: Float, pan: Float) {
        val music = music[soundId] ?: return
        if (music.isPlaying) return
        music.setPan(pan, volume)
    }

    override fun changeSound(soundId: Int, instanceId: Long, volume: Float, pitch: Float, pan: Float) {
        val sound = sounds[soundId] ?: return
        sound.setPan( instanceId, pan, volume )
        sound.setPitch( instanceId, pitch )
        sound.setVolume( instanceId, volume )
    }

    override fun playMusic(soundId: Int, looping: Boolean, volume: Float, pan: Float) {
        val music = music[soundId] ?: return
        if (music.isPlaying) return
        music.setPan(pan, volume)
        music.isLooping = looping
        music.play()
    }

    override fun playSound(soundId: Int, channel: Int, looping: Boolean, volume: Float, pitch: Float, pan: Float): Long {
        val sound = sounds[soundId] ?: return -1
        if (channel >= 0 && channel < lastPlayingSoundOnChannel.length && lastPlayingSoundOnChannel[channel] >= 0)
            sounds[lastPlayingSoundOnChannel[channel]]?.stop()

        lastPlayingSoundOnChannel[channel] = soundId

        val play = sound.play(volume, pitch, pan)
        if (looping)
            sound.setLooping(play, true)

        return play
    }

    override fun stopMusic(soundId: Int) {
        val music = music[soundId] ?: return
        if (music.isPlaying) music.stop()
    }

    override fun stopSound(soundId: Int, instanceId: Long) {
        sounds[soundId]?.stop(instanceId)
    }
}
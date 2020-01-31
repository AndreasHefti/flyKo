package com.inari.firefly.system

import com.inari.firefly.FFApp
import com.inari.firefly.FFContext
import com.inari.firefly.TestApp
import com.inari.firefly.graphics.view.ViewSystem
import com.inari.util.geom.Rectangle
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AppTest {

    private val renderEventLog = StringBuffer()
    private val renderEventListener = object : FFApp.RenderEvent.Listener {
        override fun invoke(viewId: Int, layerId: Int, clip: Rectangle) {
            renderEventLog.append("renderEvent: $viewId, $layerId, $clip \n")
        }

    }

    @Test
    fun updateRenderLoopTestWithJustBaseView() {
        TestApp
        ViewSystem
        FFContext.registerListener(FFApp.RenderEvent, renderEventListener)

        assertNotNull(ViewSystem.baseView)
        assertEquals( "0", FFContext.timer.time.toString())
        assertEquals( "", renderEventLog.toString())

        TestApp.update()

        assertEquals( "1", FFContext.timer.time.toString())
        assertEquals( "", renderEventLog.toString())

        TestApp.render()
        assertEquals(
            "renderEvent: 0, 0, [x=0,y=0,width=100,height=100] \n",
            renderEventLog.toString())
    }
}
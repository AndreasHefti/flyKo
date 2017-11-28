package com.inari.firefly

import com.inari.commons.event.EventDispatcher

object TestApp : FFApp(
    { EventDispatcher() },
    { GraphicsMock },
    { AudioMock },
    { InputMock },
    { TestTimer }
)
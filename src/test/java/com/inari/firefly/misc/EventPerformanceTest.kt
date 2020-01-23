package com.inari.firefly.misc

import com.inari.util.geom.Position
import com.inari.firefly.FFContext
import com.inari.firefly.IntFunction
import com.inari.firefly.TestApp
import com.inari.firefly.measureTime
import com.inari.util.event.Event
import org.junit.Before
import org.junit.Test

typealias TestListenerType = (Int, Int, Int) -> Unit

interface TestListenerInterface {
    operator fun invoke(i1: Int, i2: Int, i3: Int)
}

typealias TestListenerTypeObj = (Position, Position, Position) -> Unit

interface TestListenerInterfaceObj {
    operator fun invoke(i1: Position, i2: Position, i3: Position)
}

object TestEvent1 : Event<TestListenerType>(EVENT_ASPECTS.createAspect("TestEvent1")) {

    private var i1: Int = -1
    private var i2: Int = -1
    private var i3: Int = -1

    override fun notify(listener: TestListenerType) =
        listener(i1, i2, i3)

    fun send(i1: Int, i2: Int, i3: Int) {
        TestEvent1.i1 = i1
        TestEvent1.i2 = i2
        TestEvent1.i3 = i3
        FFContext.notify(this)
    }
}

object TestEvent2 : Event<TestListenerInterface>(EVENT_ASPECTS.createAspect("TestEvent2")) {

    private var i1: Int = -1
    private var i2: Int = -1
    private var i3: Int = -1

    override fun notify(listener: TestListenerInterface) =
        listener(i1, i2, i3)

    fun send(i1: Int, i2: Int, i3: Int) {
        TestEvent2.i1 = i1
        TestEvent2.i2 = i2
        TestEvent2.i3 = i3
        FFContext.notify(this)
    }
}

object TestEvent3 : Event<TestListenerTypeObj>(EVENT_ASPECTS.createAspect("TestEvent3")) {

    private var i1: Position = Position()
    private var i2: Position = Position()
    private var i3: Position = Position()

    override fun notify(listener: TestListenerTypeObj) =
        listener(i1, i2, i3)

    fun send(i1: Position, i2: Position, i3: Position) {
        TestEvent3.i1 = i1
        TestEvent3.i2 = i2
        TestEvent3.i3 = i3
        FFContext.notify(this)
    }
}

object TestEvent4 : Event<TestListenerInterfaceObj>(EVENT_ASPECTS.createAspect("TestEvent4")) {

    private var i1: Position = Position()
    private var i2: Position = Position()
    private var i3: Position = Position()

    override fun notify(listener: TestListenerInterfaceObj) =
        listener(i1, i2, i3)

    fun send(i1: Position, i2: Position, i3: Position) {
        TestEvent4.i1 = i1
        TestEvent4.i2 = i2
        TestEvent4.i3 = i3
        FFContext.notify(this)
    }
}

class EventPerformanceTest {

    @Before
    fun init() {
        TestApp
    }

    @Test
    fun test0() {
        val intFunctionLambda: IntFunction = { i -> i + 1 }
        val intFunctionInterface: IntFunction = object : IntFunction {
            override fun invoke(p1: Int): Int {
                return p1 + 1
            }
        }

        measureTime("primitive interface impl", 100_000_000_0) {
            intFunctionInterface(1)
        }

        measureTime("primitive lambda function", 100_000_000_0) {
            intFunctionLambda(1)
        }



    }

    @Test
    fun test1() {

        val testListenerType: TestListenerType = { i1, i2, i3 -> i1 + i2 + i3}
        val testListenerInterface: TestListenerInterface = object : TestListenerInterface {
            override fun invoke(i1: Int, i2: Int, i3: Int) {
                i1 + i2 + i3
            }

        }

        FFContext.registerListener(TestEvent1, testListenerType)
        FFContext.registerListener(TestEvent2, testListenerInterface)

        measureTime("primitive interface listener", 100_000_000) {
            TestEvent2.send(1, 2, 4)
        }

        measureTime("primitive subType listener", 100_000_000) {
            TestEvent1.send(1, 2, 4)
        }


    }

    @Test
    fun test2() {

        FFContext.registerListener(TestEvent1) { i1: Int, i2: Int, i3: Int -> i1 + i2 + i3 }
        FFContext.registerListener(TestEvent2, object : TestListenerInterface {
            override fun invoke(i1: Int, i2: Int, i3: Int) {
                i1 + i2 + i3
            }
        })

        measureTime("primitive subType listener with lambda", 100_000) {
            TestEvent1.send(1, 2, 4)
        }

        measureTime("primitive interface listener with lambda", 100_000) {
            TestEvent2.send(1, 2, 4)
        }
    }

    @Test
    fun test3() {
        val pos1 = Position(1, 2)
        val testListenerType: TestListenerTypeObj = { i1, i2, i3 -> i1.x + i2.x + i3.x}
        val testListenerInterface: TestListenerInterfaceObj = object : TestListenerInterfaceObj {
            override fun invoke(i1: Position, i2: Position, i3: Position) {
                i1.x + i2.x + i3.x
            }
        }

        FFContext.registerListener(TestEvent3, testListenerType)
        FFContext.registerListener(TestEvent4, testListenerInterface)

        measureTime("Obj subType listener", 100_000) {
            TestEvent3.send(pos1, pos1, pos1)
        }

        measureTime("Obj interface listener", 100_000) {
            TestEvent4.send(pos1, pos1, pos1)
        }
    }

    @Test
    fun test4() {
        val pos1 = Position(1, 2)
        FFContext.registerListener(TestEvent3) { i1: Position, i2: Position, i3: Position -> i1.x + i2.x + i3.x }
        FFContext.registerListener(TestEvent4, object : TestListenerInterfaceObj {
            override fun invoke(i1: Position, i2: Position, i3: Position) {
                i1.x + i2.x + i3.x
            }
        })

        measureTime("Obj subType listener with lambda", 100_000) {
            TestEvent3.send(pos1, pos1, pos1)
        }

        measureTime("Obj interface listener with lambda", 100_000) {
            TestEvent4.send(pos1, pos1, pos1)
        }
    }
}
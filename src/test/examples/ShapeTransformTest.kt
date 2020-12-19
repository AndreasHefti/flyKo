import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.inari.firefly.FFContext
import com.inari.firefly.entity.Entity
import com.inari.firefly.entity.EntitySystem
import com.inari.firefly.external.ShapeType
import com.inari.firefly.graphics.ETransform
import com.inari.firefly.graphics.rendering.RenderingSystem
import com.inari.firefly.graphics.shape.EShape
import com.inari.firefly.libgdx.DesktopAppAdapter
import com.inari.firefly.physics.animation.AnimationSystem
import com.inari.firefly.physics.animation.easing.EasedProperty
import com.inari.firefly.physics.animation.entity.EAnimation
import com.inari.firefly.system.FFInfoSystem
import com.inari.firefly.system.FrameRateInfo
import com.inari.util.geom.Easing

class ShapeTransformTest : DesktopAppAdapter() {

    override val title: String = "ShapeTransformTest"

    override fun init() {
        FFInfoSystem
                .addInfo(FrameRateInfo)
                .activate()
        RenderingSystem
        FFContext.loadSystem(EntitySystem)
        FFContext.loadSystem(AnimationSystem)

        Entity.buildAndActivate {
            component(ETransform) {
                position(100f, 50f)
                pivot(110f, 60f)
                rotation = 0f
            }
            component(EShape) {
                shapeType = ShapeType.RECTANGLE
                fill = true
                color(1f, 0f, 0f, 1f)
                vertices = floatArrayOf(0f, 0f, 20f, 20f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 0f
                    endValue = 180f
                    duration = 1000
                    easing = Easing.Type.LINEAR
                    propertyRef = ETransform.Property.ROTATION
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 100f
                    endValue = 600f
                    duration = 3000
                    easing = Easing.Type.SIN_IN_OUT
                    propertyRef = ETransform.Property.POSITION_X
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 110f
                    endValue = 610f
                    duration = 3000
                    easing = Easing.Type.SIN_IN_OUT
                    propertyRef = ETransform.Property.PIVOT_X
                }
            }
        }

        Entity.buildAndActivate {
            component(ETransform) {
                position(100f, 80f)
                pivot(110f, 90f)
                rotation = 0f
            }
            component(EShape) {
                shapeType = ShapeType.RECTANGLE
                fill = false
                color(1f, 0f, 0f, 1f)
                vertices = floatArrayOf(0f, 0f, 20f, 20f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 0f
                    endValue = 180f
                    duration = 1000
                    easing = Easing.Type.LINEAR
                    propertyRef = ETransform.Property.ROTATION
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 100f
                    endValue = 600f
                    duration = 3000
                    easing = Easing.Type.SIN_IN_OUT
                    propertyRef = ETransform.Property.POSITION_X
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 110f
                    endValue = 610f
                    duration = 3000
                    easing = Easing.Type.SIN_IN_OUT
                    propertyRef = ETransform.Property.PIVOT_X
                }
            }
        }


        Entity.buildAndActivate {
            component(ETransform) {
                position(100f, 150f)
                pivot(110f, 160f)
                scale(1f, 1f)
            }
            component(EShape) {
                shapeType = ShapeType.TRIANGLE
                fill = false
                color(1f, 0f, 0f, 1f)
                vertices = floatArrayOf(10f, 0f, 20f, 20f, 0f, 20f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = .5f
                    endValue = 2f
                    duration = 1000
                    easing = Easing.Type.LINEAR
                    propertyRef = ETransform.Property.SCALE_X
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = .5f
                    endValue = 2f
                    duration = 1000
                    easing = Easing.Type.LINEAR
                    propertyRef = ETransform.Property.SCALE_Y
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 100f
                    endValue = 600f
                    duration = 3000
                    easing = Easing.Type.SIN_OUT
                    propertyRef = ETransform.Property.POSITION_X
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 110f
                    endValue = 610f
                    duration = 3000
                    easing = Easing.Type.SIN_OUT
                    propertyRef = ETransform.Property.PIVOT_X
                }
            }
        }

        Entity.buildAndActivate {
            component(ETransform) {
                position(100f, 180f)
                pivot(110f, 190f)
                scale(1f, 1f)
            }
            component(EShape) {
                shapeType = ShapeType.TRIANGLE
                fill = true
                color(1f, 0f, 0f, 1f)
                vertices = floatArrayOf(10f, 0f, 20f, 20f, 0f, 20f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = .5f
                    endValue = 2f
                    duration = 1000
                    easing = Easing.Type.LINEAR
                    propertyRef = ETransform.Property.SCALE_X
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = .5f
                    endValue = 2f
                    duration = 1000
                    easing = Easing.Type.LINEAR
                    propertyRef = ETransform.Property.SCALE_Y
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 100f
                    endValue = 600f
                    duration = 3000
                    easing = Easing.Type.SIN_OUT
                    propertyRef = ETransform.Property.POSITION_X
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 110f
                    endValue = 610f
                    duration = 3000
                    easing = Easing.Type.SIN_OUT
                    propertyRef = ETransform.Property.PIVOT_X
                }
            }
        }


        Entity.buildAndActivate {
            component(ETransform) {
                position(100f, 300f)
                scale(1f, 1f)
            }
            component(EShape) {
                shapeType = ShapeType.CIRCLE
                fill = false
                segments = 20
                color(1f, 0f, 0f, 1f)
                vertices = floatArrayOf(10f, 0f, 0f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 100f
                    endValue = 600f
                    duration = 3000
                    easing = Easing.Type.SIN_IN
                    propertyRef = ETransform.Property.POSITION_X
                }
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 250f
                    endValue = 350f
                    duration = 1500
                    easing = Easing.Type.SIN_IN_OUT
                    propertyRef = ETransform.Property.POSITION_Y
                }
            }
        }

        Entity.buildAndActivate {
            component(ETransform) {
                position(100f, 350f)
                scale(1f, 1f)
            }
            component(EShape) {
                shapeType = ShapeType.ARC
                fill = false
                segments = 20
                color(1f, 0f, 0f, 1f)
                vertices = floatArrayOf(0f, 0f, 10f, 0f, 90f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 100f
                    endValue = 600f
                    duration = 3000
                    easing = Easing.Type.CIRC_IN
                    propertyRef = ETransform.Property.POSITION_X
                }
            }
        }

        Entity.buildAndActivate {
            component(ETransform) {
                position(100f, 400f)
                scale(1f, 1f)
            }
            component(EShape) {
                shapeType = ShapeType.CURVE
                fill = false
                segments = 20
                color(1f, 0f, 0f, 1f)
                vertices = floatArrayOf(0f, 0f, 10f, 10f, 5f, 5f, 20f, 0f)
            }
            component(EAnimation) {
                activeAnimation(EasedProperty) {
                    looping = true
                    inverseOnLoop = true
                    startValue = 100f
                    endValue = 600f
                    duration = 3000
                    easing = Easing.Type.CIRC_OUT
                    propertyRef = ETransform.Property.POSITION_X
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    try {
        val config = Lwjgl3ApplicationConfiguration()
        config.setResizable(true)
        config.setWindowedMode(800, 600)
        Lwjgl3Application(ShapeTransformTest(), config)
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}
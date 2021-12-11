# Fly-Ko 
(Firefly on Kotlin)

[![Build Status](https://travis-ci.org/Inari-Soft/flyKo.svg?branch=master)](https://travis-ci.org/Inari-Soft/flyKo) 
[![codecov](https://codecov.io/gh/Inari-Soft/flyKo/branch/master/graph/badge.svg)](https://codecov.io/gh/Inari-Soft/flyKo)
[![Release](https://jitpack.io/v/Inari-Soft/flyKo.svg)](https://jitpack.io/#Inari-Soft/flyKo)



### [!!! This Project has been shifted to a Kotlin multiplatform project !!!](https://github.com/AndreasHefti/flyko-lib)
           


Introduction
------------

Fly-Ko is a top-level 2D game framework for Kotlin. Focusing on intuitive API and build within stringent architecture and design principles like Component-Entity-System, Builder/DSL and component indexing for fast access.

The main goal of Firefly is to have a top-level 2D game API that comes with an in-build Component-Entity-System 
architecture that helps to organize all the game-objects, data and assets in a well-defined form and helps a lot 
on keeping the game code-base as flexible as possible for changes. Since almost everything in Fly-Ko is a component,
to create them is following always the very same builder DSL while business-code stays in their System(s). 
This leads to more flexibility when content or behavior must be changed or need to be extended. 

Fly-Ko's Entity-Component-System is implemented on-top of a small set of interfaces that defines the API to the 
lower-level libraries that can be used to render graphics, play sounds or gather or poll input events. 
Within the current version Fly-Ko uses [libgdx](https://libgdx.badlogicgames.com/) and [lwjgl](https://www.lwjgl.org/) as lower-level libraries.

Building and packaging games for desktop (Win, Mac and Linux) is currently supported and other platforms like 
Android or HTML5 should be possible but is not tested yet.

Key Features 
-----------------

- Strong backing on components and Component-Entity-System approach.

- Lightweight but powerfull and easy extendable event system for communication between Systems.  

- Independent lower level interface definition.

- Stringent component builder API with DSL support. 
  
- Indexing for component types and instances for fast access

- Multiple Views by using render-to-texture with FBO - Framebuffer Object.

- OpenGL shader support.

- Basic libraries for input, audio, tile-maps, animation, movement, contact/collision, particle, action, state, behavior and many more.

- Extendable by design. Easily add and integrate third party libraries for physics or other needs within your game.  

Code Example
--------------
<div align="center"><img src="https://github.com/Inari-Soft/flyKo/raw/master/wiki/example1.gif" alt="Result of Code Example"></div>

``` kotlin
  // Create a TextureAsset and register it to the AssetSystem but not loading yet.
  //
  // WIth this method you are able to define all your assets in one place without
  // loading the assets into memory yet. When you need them you can simply load
  // them by calling FFContext.activate(TextureAsset, "logoTexture") and dispose them
  // with FFContext.dispose(TextureAsset, "logoTexture"). The asset definition is still
  // available and can be deleted with FFContext.delete(TextureAsset, "logoTexture")
  val texAssetId = TextureAsset.build {
      name = "logoTexture"
      resourceName = "firefly/logo.png"
  }

  // Create and activate/load a SpriteAsset with reference to the TextureAsset.
  // This also implicitly loads the TextureAsset if it is not already loaded.
  val spriteId = SpriteAsset.buildAndActivate {
      // It would also be possible to use the name of the texture asset here
      // instead of the identifier. But of corse, identifier (index) gives faster access
      texture(texAssetId)
      textureRegion(0,0,32,32)
      horizontalFlip = false
      verticalFlip = false
  }

  // Create an Entity positioned on the base View on x=50/y=150, and the formerly
  // created sprite with a tint color.
  // Add also two animation, one for the alpha of the tint color and one for the
  // position on the x axis and activate everything immediately.
  val entityId = Entity.buildAndActivate {

      // add a transform component to the entity that defines the orientation of the Entity
      component(ETransform) {
          view(BASE_VIEW)
          pPosition(50, 150)
          scale(4f, 4f)
      }

      // add a sprite component to the entity
      component(ESprite) {
          sprite(spriteId)
          tint(1f, 1f, 1f, .5f)
      }

      // add an animation component to the entity that defines an animation based on
      // the alpha value of the color property of the sprite.
      //
      // Animations normally can work for itself and lifes in the AnimationSystem. But if
      // a property of an Entity-Component like ESprite defines a property value adapter,
      // an animation can be bound to this property directly to affecting the value of the property.
      component(EAnimation) {

          // with an active easing animation on the sprite alpha blending value...
          activeAnimation(EasedProperty) {
              easing = Easing.Type.LINEAR
              startValue = 0f
              endValue = 1f
              duration = 3000
              looping = true
              inverseOnLoop = true

              // that is connected to the alpha value of the sprite of the entity
              propertyRef = ESprite.Property.TINT_ALPHA
          }

          // and with an active easing animation on the sprites position on the x axis...
          activeAnimation(EasedProperty) {
              easing = Easing.Type.BACK_OUT
              startValue = 50f
              endValue = 400f
              duration = 1000
              looping = true
              inverseOnLoop = true

              // that is connected to the position value on the x axis of the entities transform data
              propertyRef = ETransform.Property.POSITION_X
          }
      }
  }
```



Let's Get Started with Fly-Ko
------------------------------

Want to give it a try? Give me half an hour to bring your [hands on Fly-Ko](https://github.com/Inari-Soft/flyKo/wiki) and who knows maybe you want more... 

<div align="center"><img src="https://github.com/Inari-Soft/flyKo/raw/master/wiki/microdash_small.gif" alt="Micro Dash (WorkInProgress)"></div>

Install
-------

Fly-Ko is available from Jitpack repository as release candidate version. 
If you want to use the newest development version use "-SNAPSHOT" as version.

#### Maven

``` xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Inari-Soft</groupId>
    <artifactId>FlyKo</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle

``` groovy

repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile group: 'com.github.Inari-Soft', name: 'FlyKo', version:'1.0.0'
}
```

License
--------

Fly-Ko is licensed under the Apache 2 License, meaning you can use it free of charge, 
without strings attached in commercial and non-commercial projects.





# Fly-Ko 
(Firefly on Kotlin)

[![Build Status](https://travis-ci.org/Inari-Soft/flyKo.svg?branch=master)](https://travis-ci.org/Inari-Soft/flyKo) 
[![codecov](https://codecov.io/gh/Inari-Soft/flyKo/branch/master/graph/badge.svg)](https://codecov.io/gh/Inari-Soft/flyKo)
[![Release](https://jitpack.io/v/Inari-Soft/flyKo.svg)](https://jitpack.io/#Inari-Soft/flyKo)
           


Introduction
------------

Fly-Ko is a top-level 2D game framework for Kotlin. Focusing on intuitive API and build within stringent architecture and design principles like Component-Entity-System, Builder/DSL and component indexing for fast access.

The main idea of Firefly is to have a top-level 2D game API that comes with an in-build Component-Entity-System 
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

- Strong backing on Component and Component-Entity-System approach.

- Lightweight but power-full and easy extendable event system for communication between Systems.  

- Independent Lower Level interface definition

- Stringent Component builder API with DSL support. 
  
- Indexing for Component types and instances for fast access

- Multiple Views 

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
      ff_Name = "logoTexture"
      ff_ResourceName = "firefly/logo.png"
  }

  // Create and activate/load a SpriteAsset with reference to the TextureAsset.
  // This also implicitly loads the TextureAsset if it is not already loaded.
  val spriteId = SpriteAsset.buildAndActivate {
      // It would also be possible to use the name of the texture asset here
      // instead of the identifier. But of corse, identifier (index) gives faster access
      ff_Texture(texAssetId)
      ff_TextureRegion(0,0,32,32)
      ff_HorizontalFlip = false
      ff_VerticalFlip = false
  }

  // Create an Entity positioned on the base View on x=50/y=150, and the formerly
  // created sprite with a tint color.
  // Add also two animation, one for the alpha of the tint color and one for the
  // position on the x axis and activate everything immediately.
  val entityId = Entity.buildAndActivate {

      // add a transform component to the entity that defines the orientation of the Entity
      ff_With(ETransform) {
          ff_View(BASE_VIEW)
          ff_Position(50, 150)
          ff_Scale(4f, 4f)
      }

      // add a sprite component to the entity
      ff_With(ESprite) {
          ff_Sprite(spriteId)
          ff_Tint(1f, 1f, 1f, .5f)
      }

      // add an animation component to the entity that defines a value based animation
      // and is bound to the value of the alpha value of the sprites tint color property.
      //
      // Animations normally can work for itself and live in the AnimationSystem. But if
      // a property of an Entity-Component like ESprite defines a property value adapter,
      // an animation can be bound to this property to affecting the value of the property directly.
      ff_With(EAnimation) {

          // with an active easing animation on the sprite alpha blending value...
          ff_WithActiveAnimation(EasedProperty) {
              ff_Easing = Easing.Type.LINEAR
              ff_StartValue = 0f
              ff_EndValue = 1f
              ff_Duration = 3000
              ff_Looping = true
              ff_InverseOnLoop = true

              // that is connected to the alpha value of the sprite of the entity
              ff_PropertyRef = ESprite.Property.TINT_ALPHA
          }

          // and with an active easing animation on the sprites position on the x axis...
          ff_WithActiveAnimation(EasedProperty) {
              ff_Easing = Easing.Type.BACK_OUT
              ff_StartValue = 50f
              ff_EndValue = 400f
              ff_Duration = 1000
              ff_Looping = true
              ff_InverseOnLoop = true

              // that is connected to the position value on the x axis of the entities transform data
              ff_PropertyRef = ETransform.Property.POSITION_X
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
    <version>1.0.0-rc</version>
</dependency>
```

#### Gradle

``` groovy

repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile group: 'com.github.Inari-Soft', name: 'FlyKo', version:'1.0.0-rc'
}
```

License
--------

Fly-Ko is licensed under the Apache 2 License, meaning you can use it free of charge, 
without strings attached in commercial and non-commercial projects.





# FlyKo


[![Build Status](https://travis-ci.org/Inari-Soft/flyKo.svg?branch=master)](https://travis-ci.org/Inari-Soft/flyKo) 

Projects this depends on:
[commons](https://github.com/Inari-Soft/inari-commons) [![Build Status](https://travis-ci.org/Inari-Soft/inari-firefly.svg?branch=master)](https://travis-ci.org/Inari-Soft/inari-commons)

**Introduction**

FlyKo is the core of [Firefly](https://github.com/Inari-Soft/inari-firefly) for Kotlin Language

Firefly is a top level 2D game engine framework for Kotlin/Java focusing on intuitive API build on stringent architecture and design.
What makes it different to other java gaming frameworks is its focus on build and manage components and game objects within a component-
entity-system approach and being independent from low level implementation(s).

The main idea of Firefly is to have a top-level 2D game API that comes with a in-build Component-Entity-System architecture that helps
organizing all the game-objects, data and assets in a well defined form and also helps a lot on keeping the game codebase as flexible 
as possible for changes, modify/adding new behavior during the development cycle. What is one of the most impressive benefits of a 
Component-Entity-System based architecture and design approach.
Firefly is implemented on-top of other existing java gaming frameworks like lwjgl or libgdx with the flexibility to change the lower level 
implementation while reusing as much of the game code as possible.


Key features

- Strong backing on Component and Component-Entity-System approach.
  Almost everything within Firefly is a Component or a Entity (composite of components) or a System

- Lightweight but power-full and easy extendable event system for communication between Systems.  

- Independent Lower Level interface definition
  There are a few interface definitions that must be implemented to implement Firefly within a lower level library like lwjgl or libgdx.
  All code that is written against the Firefly API is not affected by the change of the lower level library. 
  Until now only a project with an implementation for libgdx is supported.

- Stringent Component builder API
  
Code example:

```
  // Create a TextureAsset and register it to the AssetSystem but no loading yet
  
  texAssetId = TextureAsset.build {
    ff_Name = "logoTexture"
    ff_ResourceName = "firefly/inari.png"
    ff_Width = 200
    ff_Height = 100
  }
  
  // Create and activate/load a SpriteAsset with reference to the TextureAsset
  // also implicitly loads the TextureAsset
  
  spriteId = SpriteAsset.buildAndActivate {
    ff_Texture.id = texAssetId
    ff_TextureRegion = Rectangle(0,0,10,10)
  }
  
  // Create an Entity with a position on the base View and the created sprite
  // heaving a tint color with animated alpha value
  
  Entity.build {
  
    with(ETransform) {
        ff_View.index = 0
        ff_Position = Position(100,100)
    }
    
    with(ESprite) {
        ff_Sprite.id = spriteId
        ff_Tint.setFrom(RGBColor(1f, 1f, 1f, .5f))
    }
    
    with(EAnimation) {
        withActive(EasingAnimated) {
            ff_EasingType = Easing.Type.LINEAR
            ff_StartValue = 0f
            ff_EndValue = 1f
            ff_Duration = 1000
            ff_PropertyRef = ESprite.Property.TINT_ALPHA
        }
    }
  }

```

- Indexing for Component types and instances for fast access
  Firefly comes with an indexing system that allows to index Java types (Class types) within a defined root type on one hand and on the other
  to index instances (objects) of a specified type. All Components, Entities and Systems are indexed by type and mostly, if needed also by instance
  to guarantee fast access.
  
**NOTE:**

FlyKo is still under construction

This is the core API with no lower level implementation. Just the API. 
At the moment there is only one lower level implementation for the API
within the famous libGDX in [FlyKoGDX](https://github.com/Inari-Soft/flyKoGDX)

If you want to just to use the Firefly API with a libGDX implementation, 
this is your starting point.

**Installation**
  
  - TODO



# Fly-Ko (Firefly on Kotlin)


[![Build Status](https://travis-ci.org/Inari-Soft/flyKo.svg?branch=master)](https://travis-ci.org/Inari-Soft/flyKo) 
[![codecov](https://codecov.io/gh/Inari-Soft/flyKo/branch/master/graph/badge.svg)](https://codecov.io/gh/Inari-Soft/flyKo)


Introduction
------------



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

- Stringent Component builder API with DSL support. Use Fly-Ko's builder DSL to build and compose components.
  
Code example:

``` kotlin
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
    ff_Texture(texAssetId)
    ff_TextureRegion(0,0,10,10)
  }
  
  // Create an Entity positioned on the base View on x=100/y=100 and the formarly 
  // created sprite with a tint color that has animated alpha value
  
  Entity.build {
  
    // add an ETransform component to the entity
    ff_With(ETransform) {
        ff_View(BASE_VIEW)
        ff_Position(100, 100)
    }
    
    // add an ESprite component to the entity
    ff_With(ESprite) {
        ff_Sprite(spriteId)
        ff_Tint(1f, 1f, 1f, .5f)
    }
    
    // add an EAnimation component to the entity
    ff_With(EAnimation) {

        // with an active easing animation... 
        ff_WithActive(EasingAnimated) {
            ff_EasingType = Easing.Type.LINEAR
            ff_StartValue = 0f
            ff_EndValue = 1f
            ff_Duration = 1000

            // that is connected to the alpha value of the sprite of the entity
            ff_PropertyRef = ESprite.Property.TINT_ALPHA
        }
    }
  }

```

- Indexing for Component types and instances for fast access
  Firefly comes with an indexing system that allows to indexed Java types (Class types) within a defined root type on one hand and on the other
  to index instances (objects) of a specified type. All Components, Entities and Systems are indexed by type and mostly, if needed also by the instance
  to guarantee fast access.
  

  
NOTES:
********

FlyKo is still under construction

This repository contains the core API with a reference implementation on libGDX and is fully runnable with no extra extensions and only a few examples

If you want to just use the Firefly API with a libGDX implementation, this is your starting point.

*****

Installation
----------------
  
  - TODO



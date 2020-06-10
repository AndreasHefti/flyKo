# Fly-Ko (Firefly on Kotlin)


[![Build Status](https://travis-ci.org/Inari-Soft/flyKo.svg?branch=master)](https://travis-ci.org/Inari-Soft/flyKo) 
[![codecov](https://codecov.io/gh/Inari-Soft/flyKo/branch/master/graph/badge.svg)](https://codecov.io/gh/Inari-Soft/flyKo)


Introduction
------------

Fly-Ko is a top level 2D game framework for Kotlin. Focusing on intuitive API and build within stringent architecture and design principles like Component-Entity-System, Builder/DSL and component indexing for fast access.

The main idea of Firefly is to have a top-level 2D game API that comes with an in-build Component-Entity-System architecture that helps organizing all the game-objects, data and assets in a well defined form and also helps a lot on keeping the game code-base as flexible as possible for changes. Since almost everything in Fly-Ko is a component, to create the them is following always the very same builder DSL while business-code stays in their System(s). This leads to more flexibility when content or behavior must be changed or need to be extended. 

Fly-Ko's Entity-Component-System is implemented on-top of a small set of interfaces that defines the API to the lower-level libraries that can be used to render graphics, play sounds or gather or poll input events. Within the current version Fly-Ko uses libgdx and lwjgl as lower-level libraries.

Key features of Fly-Ko 2D game API:

- Strong backing on Component and Component-Entity-System approach.
  Almost everything within Firefly is a Component or a Entity (composite of components) or a System

- Lightweight but power-full and easy extendable event system for communication between Systems.  

- Independent Lower Level interface definition
  There are a few interface definitions that must be implemented to implement Firefly within a lower level library like lwjgl or libgdx.
  All code that is written against the Firefly API is not affected by the change of the lower level library. 
  Until now only a project with an implementation for libgdx is supported.

- Stringent Component builder API with DSL support. Use Fly-Ko's builder DSL to build and compose components.

- Indexing for Component types and instances for fast access
  Firefly comes with an indexing system that allows to indexed Java types (Class types) within a defined root type on one hand and on the other
  to index instances (objects) of a specified type. All Components, Entities and Systems are indexed by type and mostly, if needed also by the instance
  to guarantee fast access.
  
- Multiple Views 
  Views on Fly-Ko are like windows to separated worlds within the main game window that can be placed at the main game window and render its own (layered) world. And of course views are components and can be build like any other component in Fly-Ko. 
  Under the hood, Views uses OpenGL's FBO (Frame Buffer Object) to render each world-view to a texture and render all this textures to the main game window at the end.
  
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

Let's Get Started with Fly-Ko
------------------------------

Want to give it a try? Give me half an hour to bring your hands on Fly-Ko and who knows maybe you want more... 


Installation
----------------
  
  - TODO



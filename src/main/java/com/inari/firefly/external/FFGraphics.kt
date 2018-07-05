/*******************************************************************************
 * Copyright (c) 2015 - 2016, Andreas Hefti, inarisoft@yahoo.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inari.firefly.external

import com.inari.commons.lang.list.DynArrayRO
import com.inari.util.geom.Rectangle

/** This defines the low level API interface for all graphical functions used by the firefly API.
 * The [ViewEventListener] implementation has to deal with [View](s) on lower level API.
 *
 * There are two kind of Views; The BaseView that always is the Window-View. No matter if there are more Views available, the BaseView is always existing.
 * And any number of VirtualViews. A VirtualView is a rectangular region within the BaseView that defines its own 2D space (camera/view).
 * Usually a VirtualView can be implemented within Viewports or FBO's within rendering to textures on GPU level.
 * For more Information about Views see [ViewSystem]
 *
 */
interface FFGraphics /*ViewEventListener */ {

    /** Use this to get the actual screen width
     * @return the actual screen width
     */
    val screenWidth: Int

    /** Use this to get the actual screen height
     * @return the actual screen height
     */
    val screenHeight: Int

    /** This is called from the firefly API when a texture is created/loaded and should be loaded into the GPU
     *
     * @param data The texture DAO
     * @return the texture identifier to identify the texture on lower level API and
     *         the width and height of the texture within a Triple
     */
    fun createTexture(data: TextureData): Triple<Int, Int, Int>

    /** This is called from the firefly API when a texture is disposed and should be deleted from GPU.
     * and must release and delete the texture on GPU level
     *
     * @param textureId identifier of the texture to dispose.
     */
    fun disposeTexture(textureId: Int)

    /** This is called from the firefly API when a sprite is created/loaded and gives an identifier for that sprite.
     *
     * @param data the sprite DAO
     * @return the sprite identifier to identify the sprite on lower level API.
     */
    fun createSprite(data: SpriteData): Int

    /** This is called from the firefly API when a sprite is disposed
     * and must release and delete the sprite on lower level
     *
     * @param the sprite identifier of the texture to dispose.
     */
    fun disposeSprite(spriteId: Int)

    /** This is called from the firefly API when a shader script is created/loaded and gives an identifier for that shader script.
     *
     * @param data the shader DAO
     * @return the shader identifier to identify the shader on lower level API.
     */
    fun createShader(data: ShaderData): Int

    /** This is called from the firefly API when a shader script is disposed
     * and must release and delete the shader script on GPU level
     *
     * @param shaderId identifier of the shader to dispose.
     */
    fun disposeShader(shaderId: Int)

    /** This is called form the firefly API before rendering to a given [ViewData] and must
     * prepare all the stuff needed to render the that [ViewData] on following renderXXX calls.
     *
     * @param view the [ViewData] that is starting to be rendered
     * @param clear indicates whether the [ViewData] should be cleared with the vies clear-color before rendering or not
     */
    fun startRendering(view: ViewData, clear: Boolean)

    /** This is called form the firefly API to render a created sprite on specified position to the actual [ViewData]
     *
     * @param renderableSprite the sprite DAO
     * @param xpos the x-axis position in the 2D world of the actual [ViewData]
     * @param ypos the y-axis position in the 2D world of the actual [ViewData]
     */
    fun renderSprite(renderableSprite: SpriteRenderable, xpos: Float, ypos: Float)

    /** This is called form the firefly API to render a created sprite on specified position and scale to the actual [ViewData]
     *
     * @param renderableSprite the sprite DAO
     * @param xpos the x-axis position in the 2D world of the actual [ViewData]
     * @param ypos the y-axis position in the 2D world of the actual [ViewData]
     * @param scale the x-axis and y-axis scale for the sprite to render
     */
    fun renderSprite(renderableSprite: SpriteRenderable, xpos: Float, ypos: Float, scale: Float)

    /** This is called form the firefly API to render a created sprite with specified [TransformData] to the actual [ViewData]
     *
     * @param renderableSprite the sprite DAO
     * @param transform [TransformData] DAO containing all transform data to render the sprite like: position-offset, scale, pivot, rotation
     */
    fun renderSprite(renderableSprite: SpriteRenderable, transform: TransformData)

    /** This is called form the firefly API to render a shape. See [ShapeData] for more information about the data structure of shapes.
     *
     * @param data [ShapeData] DAO
     */
    fun renderShape(data: ShapeData)

    /** This is called form the firefly API to render a shape with given [TransformData].
     * See [ShapeData] for more information about the data structure of shapes.
     *
     * @param data [ShapeData] DAO
     * @param transform [TransformData] DAO
     */
    fun renderShape(data: ShapeData, transform: TransformData)

    /** This is called form the firefly API to notify the end of rendering for a specified [ViewData].
     * @param view [ViewData] that is ending to be rendered
     */
    fun endRendering(view: ViewData)

    fun flush(virtualViews: DynArrayRO<ViewData>)

    fun getScreenshotPixels(area: Rectangle): ByteArray

}

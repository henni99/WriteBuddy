package com.henni.handwriting.tool

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint

/**
 * Interface representing the touch event handling for a drawing tool.
 * It defines methods for handling touch events such as initialization, start, move, end, and drawing on the canvas.
 */

@Stable
interface ToolTouchEvent {

    /**
     * Initializes the touch event
     */
    fun onTouchInitialize()

    /**
     * Handles the start of a touch event.
     * This method is called when the user first touches the canvas.
     *
     * @param canvas The canvas where the touch event is taking place. Can be `null` if not provided.
     * @param offset The offset from the initial touch position.
     * @param paint The paint object that is used for drawing.
     */
    fun onTouchStart(
        canvas: Canvas? = null,
        offset: Offset = Offset.Zero,
        paint: Paint = Paint(),
    )

    /**
     * Handles the movement of the touch event.
     * This method is called when the user moves their finger or pointer on the canvas.
     *
     * @param canvas The canvas where the touch event is taking place. Can be `null` if not provided.
     * @param previousOffset The previous offset before the move.
     * @param currentOffset The current offset during the move.
     * @param paint The paint object that is used for drawing.
     */
    fun onTouchMove(
        canvas: Canvas? = null,
        previousOffset: Offset = Offset.Zero,
        currentOffset: Offset = Offset.Zero,
        paint: Paint = Paint(),
    )

    /**
     * Handles the end of the touch event.
     * This method is called when the user lifts their finger or pointer from the canvas.
     *
     * @param canvas The canvas where the touch event occurred. Can be `null` if not provided.
     * @param paint The paint object that is used for drawing.
     */
    fun onTouchEnd(
        canvas: Canvas? = null,
        paint: Paint = Paint(),
    )

    /**
     * Draws onto the canvas using the provided paint object.
     * This method can be called during any phase of the touch event to draw the result of the touch interaction.
     *
     * @param canvas The canvas where the drawing will be performed.
     * @param paint The paint object that is used for drawing.
     * @param isMultiTouch A boolean indicating if the touch event involves multiple pointers (multi-touch).
     */
    fun onDrawIntoCanvas(
        canvas: Canvas,
        paint: Paint = Paint(),
        isMultiTouch: Boolean = false
    )
}
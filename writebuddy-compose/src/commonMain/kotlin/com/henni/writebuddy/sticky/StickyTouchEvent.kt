package com.henni.writebuddy.sticky

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset

/**
 * A stable interface that defines touch event behaviors for sticky items.
 * The interface includes methods to handle touch events like initialization, start, move, and end.
 */

@Stable
interface StickyTouchEvent {
    /**
     * Initializes the touch event
     */
    fun onTouchInitialize()

    /**
     * Handles the start of a touch event.
     * This method is called when the user first touches the canvas.
     *
     * @param offset The offset from the initial touch position.
     */
    fun onTouchStart(
        offset: Offset = Offset.Zero,
    )

    /**
     * Handles the movement of the touch event.
     * This method is called when the user moves their finger or pointer on the canvas.
     *
     * @param previousOffset The previous offset before the move.
     * @param currentOffset The current offset during the move.
     */
    fun onTouchMove(
        previousOffset: Offset = Offset.Zero,
        currentOffset: Offset = Offset.Zero,
    )

    /**
     * Handles the end of the touch event.
     * This method is called when the user lifts their finger or pointer from the canvas.
     */
    fun onTouchEnd()

}
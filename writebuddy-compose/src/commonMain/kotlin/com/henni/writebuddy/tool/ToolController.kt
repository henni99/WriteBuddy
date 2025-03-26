package com.henni.writebuddy.tool

import androidx.compose.runtime.Stable

/**
 * Interface representing a tool controller for managing touch events and invalidation ticks.
 * This interface is designed for tools that interact with a canvas, such as drawing tools, erasers, or pointers.
 * It includes methods for managing touch interactions and updating the invalidate state of the canvas.
 */

@Stable
interface ToolController {

    /**
     * The touch event associated with the tool.
     * This event handles all touch-related interactions, such as starting, moving, and ending touch events.
     */
    val touchEvent: ToolTouchEvent

    /**
     * The number of invalidate ticks for the tool.
     * This value is used to trigger a redraw of the canvas when the tool's state has changed.
     * It can be incremented each time the tool updates to notify the canvas that it needs to be redrawn.
     */
    val invalidateTick: Int

    /**
     * A boolean indicating whether the tool is currently being multi-touched.
     * This property tracks whether multiple touch points are currently interacting with the tool at once.
     */
    val isMultiTouched: Boolean

    /**
     * Updates the invalidate tick count for the tool.
     * This function is used to increment the invalidate tick and signal that the canvas should be updated.
     */
    fun updateInvalidateTick()

}
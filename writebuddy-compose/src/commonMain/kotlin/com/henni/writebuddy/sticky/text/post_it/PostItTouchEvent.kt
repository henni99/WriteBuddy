package com.henni.writebuddy.sticky.text.post_it

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.henni.writebuddy.sticky.StickyItemController
import com.henni.writebuddy.sticky.StickyTouchEvent
import com.henni.writebuddy.sticky.text.common.StickyTextItem

/**
 * Handles touch events for the Post-It sticky note, including touch initialization, start, move, and end.
 * This class manages the interaction with the sticky note and allows for adding a new sticky note
 * item when a tap is detected.
 *
 * @param controller The StickyItemController responsible for managing sticky note items.
 */

internal class PostItTouchEvent internal constructor(
    private val controller: StickyItemController,
) : StickyTouchEvent {

    /** Stores the initial touch point. */
    private var firstPoint by mutableStateOf(Offset.Zero)

    /** Stores the last recorded touch point. */
    private var lastPoint by mutableStateOf(Offset.Zero)

    /** Indicates whether the touch event is a tap action. */
    private var isTap = false

    /**
     * Initializes the touch event flags and resets the touch points to zero.
     */
    override fun onTouchInitialize() {
        isTap = false
        firstPoint = Offset.Zero
        lastPoint = Offset.Zero
    }

    /**
     * Handles the start of a touch event. It sets the initial touch point and marks it as a tap.
     *
     * @param offset The position of the touch when it starts.
     */
    override fun onTouchStart(offset: Offset) {
        isTap = true
        firstPoint = offset
    }

    /**
     * Handles the movement of a touch event. It updates the last touch position and marks the touch
     * as no longer a tap (if the touch moves).
     *
     * @param previousOffset The previous position of the touch.
     * @param currentOffset The current position of the touch.
     */
    override fun onTouchMove(previousOffset: Offset, currentOffset: Offset) {
        isTap = false
        lastPoint = currentOffset
    }

    /**
     * Handles the end of a touch event. If it was a tap, it adds a new sticky note item.
     * If the sticky note is currently focused, it clears the focus instead.
     */
    override fun onTouchEnd() = with(controller) {
        if (isTap) {

            if(isFocusNow()) {
                clearAllFocus()
                updateInvalidateTick()
                return
            }

            addStickyItem(
                StickyTextItem(
                    firstPoint = firstPoint,
                    type = type,
                    property = postItProperty
                )
            )
        }
    }
}



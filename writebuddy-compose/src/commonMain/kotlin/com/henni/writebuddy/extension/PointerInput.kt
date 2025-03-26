package com.henni.writebuddy.extension

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach

/**
 * Detects transform gestures such as zoom and pan, with support for multi-touch gestures.
 * This function handles gesture start, ongoing changes (zoom and pan), and gesture end.
 *
 * @param onGesture A lambda function that is called on every gesture update. It provides:
 *                  - `change`: The pointer input change.
 *                  - `isMultiTouch`: Boolean indicating whether the gesture involves multiple touches.
 * @param onGestureStart A lambda function called when the gesture starts, providing the initial position.
 * @param onGestureEnd A lambda function called when the gesture ends, indicating whether it was multi-touch.
 * @param onGestureCancel A lambda function called when the gesture is cancelled.
 */

 suspend fun PointerInputScope.detectTransformGestures(
    onGesture: (change: PointerInputChange, isMultiTouch: Boolean) -> Unit,
    onGestureStart: (Offset) -> Unit = {},
    onGestureEnd: (isMultiTouch: Boolean) -> Unit = {},
    onGestureCancel: () -> Unit = {},
) = awaitEachGesture {
    val firstDown = awaitFirstDown(requireUnconsumed = false)
    firstDown.consume()

    // Call the gesture start callback with the initial position
    onGestureStart(firstDown.position)

    var isMultiTouch = false

    // Handle each pointer event until released
    forEachPointerEventUntilReleased(
        onCancel = { onGestureCancel() },
    ) { event, isTouchSlopPast ->

        // Check if the gesture has passed the touch slop threshold and process zoom and pan
        if (isTouchSlopPast) {

            // Trigger the gesture callback if there is a zoom or pan change
            onGesture(
                event.changes[0],
                isMultiTouch,
            )

            // Consume the input changes that have moved
            event.changes.fastForEach {
                if (it.positionChanged()) {
                    it.consume()
                }
            }
        }

        // Detect if the gesture is multi-touch (more than one pointer)
        if (event.changes.size > 1) {
            isMultiTouch = true
        }

        // Cancel the gesture if there was previously a multi-touch, but the event now has only one pointer
        val cancelGesture = isMultiTouch && event.changes.size == 1
        !cancelGesture
    }

    // Call the gesture end callback with multi-touch status
    onGestureEnd(isMultiTouch)
}


/**
 * Detects transform gestures such as zoom and pan, with support for multi-touch gestures.
 * This function handles gesture start, ongoing changes (zoom and pan), and gesture end.
 *
 * @param onGesture A lambda function that is called on every gesture update. It provides:
 *                  - `zoomChange`: The change in zoom scale.
 *                  - `panChange`: The offset change for panning.
 *                  - `change`: The pointer input change.
 *                  - `isMultiTouch`: Boolean indicating whether the gesture involves multiple touches.
 * @param onGestureStart A lambda function called when the gesture starts, providing the initial position.
 * @param onGestureEnd A lambda function called when the gesture ends, indicating whether it was multi-touch.
 * @param onGestureCancel A lambda function called when the gesture is cancelled.
 */

suspend fun PointerInputScope.detectTransformGestures(
    onGesture: (zoomChange: Float, panChange: Offset, change: PointerInputChange, isMultiTouch: Boolean) -> Unit,
    onGestureStart: (Offset) -> Unit = {},
    onGestureEnd: (isMultiTouch: Boolean) -> Unit = {},
    onGestureCancel: () -> Unit = {},
) = awaitEachGesture {
    val firstDown = awaitFirstDown(requireUnconsumed = false)
    firstDown.consume()

    // Call the gesture start callback with the initial position
    onGestureStart(firstDown.position)

    var isMultiTouch = false

    // Handle each pointer event until released
    forEachPointerEventUntilReleased(
        onCancel = { onGestureCancel() },
    ) { event, isTouchSlopPast ->

        // Check if the gesture has passed the touch slop threshold and process zoom and pan
        if (isTouchSlopPast) {
            val zoomChange = event.calculateZoom()
            val panChange = event.calculatePan()

            // Trigger the gesture callback if there is a zoom or pan change
            if (zoomChange != 1f || panChange != Offset.Zero) {
                onGesture(
                    zoomChange,
                    panChange,
                    event.changes[0],
                    isMultiTouch,
                )

                // Consume the input changes that have moved
                event.changes.fastForEach {
                    if (it.positionChanged()) {
                        it.consume()
                    }
                }
            }
        }

        // Detect if the gesture is multi-touch (more than one pointer)
        if (event.changes.size > 1) {
            isMultiTouch = true
        }

        // Cancel the gesture if there was previously a multi-touch, but the event now has only one pointer
        val cancelGesture = isMultiTouch && event.changes.size == 1
        !cancelGesture
    }

    // Call the gesture end callback with multi-touch status
    onGestureEnd(isMultiTouch)
}


/**
 * Iterates over pointer events until the event is released or cancelled.
 * This function checks if the gesture has exceeded the touch slop threshold
 * and calls the provided action for each pointer event.
 *
 * @param onCancel A lambda function that is called if the gesture is cancelled (e.g., if the pointer is consumed).
 * @param action A lambda function that is called for each pointer event, passing the event and a boolean indicating
 *               whether the touch slop threshold has been passed. The lambda should return a boolean indicating
 *               whether to continue processing the event.
 */
private suspend fun AwaitPointerEventScope.forEachPointerEventUntilReleased(
    onCancel: () -> Unit,
    action: (event: PointerEvent, isTouchSlopPast: Boolean) -> Boolean,
) {
    // Create a TouchSlopChecker to track whether the touch gesture exceeds the touch slop threshold
    val touchSlop = TouchSlopChecker(viewConfiguration.touchSlop)
    do {
        // Await the next pointer event (Main pass)
        val mainEvent = awaitPointerEvent(pass = PointerEventPass.Main)

        // Check if any pointer event has been consumed (indicating cancellation)
        if (mainEvent.changes.fastAny { it.isConsumed }) {
            // Call the cancel callback if the event is consumed
            onCancel()
            break
        }

        // Check if the touch gesture has exceeded the touch slop threshold
        val isTouchSlopPast = touchSlop.isPastThreshold(mainEvent)

        // Call the provided action with the current event and touch slop status
        // If the action returns false, stop processing further events
        val canContinue = action(mainEvent, isTouchSlopPast)
        if (!canContinue) {
            break
        }

        // If the touch slop threshold is not passed, continue waiting for more events
        if (isTouchSlopPast) {
            continue
        }
    } while (mainEvent.changes.fastAny { it.pressed }) // Continue processing while the pointer is pressed
}


/**
 * [TouchSlopChecker] checks if a touch gesture has exceeded the threshold for pan movement.
 * It is typically used to determine if the user intended to scroll or move an object,
 * by comparing the movement distance with a predefined threshold.
 *
 * @property threshold The minimum distance (in pixels) that the pan gesture must exceed to be considered past the threshold.
 */

private class TouchSlopChecker internal constructor(
    private val threshold: Float,
) {
    private var pan = Offset.Zero
    private var isPastThreshold = false

    /**
     * Determines if the touch gesture has moved past the threshold distance.
     *
     * @param event The pointer event that contains the touch gesture information.
     * @return `true` if the touch gesture has exceeded the threshold, `false` otherwise.
     */
    fun isPastThreshold(event: PointerEvent): Boolean {
        // If the gesture has already passed the threshold, return true
        if (isPastThreshold) {
            return true
        }

        // If there are multiple pointers (e.g., multi-touch), consider the gesture past the threshold
        if (event.changes.size > 1) {
            isPastThreshold = true
        } else {
            // Calculate the pan movement and check if it exceeds the threshold
            pan += event.calculatePan()
            isPastThreshold = pan.getDistance() > threshold
        }

        return isPastThreshold
    }
}



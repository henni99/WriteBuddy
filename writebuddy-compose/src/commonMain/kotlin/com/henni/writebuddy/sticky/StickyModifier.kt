package com.henni.writebuddy.sticky

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.henni.writebuddy.extension.detectTransformGestures

/**
 * A custom modifier function that enables "sticky mode" for a given view or composable.
 * Sticky mode allows interactive gestures such as touch events to be handled by the `StickyItemController`.
 *
 * @param controller The controller responsible for handling touch events for sticky items.
 * @param isEnabled A flag indicating whether sticky mode is enabled. Defaults to `true`.
 * @return The modified modifier, either with or without touch event handling based on the `isEnabled` flag.
 */

fun Modifier.useStickyMode(
  controller: StickyItemController,
  isEnabled: Boolean = true,
) = this then (
  if (!isEnabled) {
    Modifier
  } else {
    Modifier
      .pointerInput(Unit) {
        detectTransformGestures(
          onGestureStart = { offset ->
            controller.touchEvent.onTouchStart(
              offset = offset,
            )
          },
          onGesture = { change, isMultiTouch ->

            controller.touchEvent.onTouchMove(
              previousOffset = change.previousPosition,
              currentOffset = change.position,
            )
          },
          onGestureEnd = { isMultiTouch ->

            controller.touchEvent.onTouchEnd()
          },
          onGestureCancel = {
            controller.touchEvent.onTouchInitialize()
          },
        )
      }
  }
  )

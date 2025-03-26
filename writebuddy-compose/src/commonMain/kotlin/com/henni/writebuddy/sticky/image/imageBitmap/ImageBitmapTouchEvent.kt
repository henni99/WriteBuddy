package com.henni.writebuddy.sticky.image.imageBitmap

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.henni.writebuddy.sticky.StickyItemController
import com.henni.writebuddy.sticky.StickyTouchEvent

/**
 * Handles touch events for an image bitmap item.
 * This class manages user interactions such as tapping, moving, and selecting an image bitmap.
 *
 * @property controller The controller responsible for managing sticky items.
 */

internal class ImageBitmapTouchEvent internal constructor(
  private val controller: StickyItemController,
) : StickyTouchEvent {

  /** Stores the initial touch point. */
  private var firstPoint by mutableStateOf(Offset.Zero)

  /** Stores the last recorded touch point. */
  private var lastPoint by mutableStateOf(Offset.Zero)

  /** Indicates whether the touch event is a tap action. */
  private var isTap = false

  /** Initializes touch-related states. */
  override fun onTouchInitialize() {
    isTap = false
    firstPoint = Offset.Zero
    lastPoint = Offset.Zero
  }

  /**
   * Handles the start of a touch event.
   *
   * @param offset The position where the touch event started.
   */
  override fun onTouchStart(offset: Offset) {
    isTap = true
    firstPoint = offset
  }

  /**
   * Handles touch movement events.
   *
   * @param previousOffset The previous touch position.
   * @param currentOffset The current touch position.
   */
  override fun onTouchMove(previousOffset: Offset, currentOffset: Offset) {
    isTap = false
    lastPoint = currentOffset
  }

  /**
   * Handles the end of a touch event.
   * If the event is a tap, it either clears focus or adds a new image bitmap item.
   */
  override fun onTouchEnd() = with(controller) {
    if (isTap) {
      if (isFocusNow()) {
        clearAllFocus()
        updateInvalidateTick()
        return
      }

      addStickyItem(
        ImageBitmapItem(
          firstPoint = firstPoint,
          type = type,
          property = bitmapImageProperty,
        ),
      )
    }
  }
}

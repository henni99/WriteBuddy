package com.henni.writebuddy.tool

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * Interface representing the touch event handling for a drawing tool.
 * It defines methods for handling touch events such as initialization, start, move, end, and drawing on the canvas.
 */

@Stable
interface ToolTouchEvent {

  /**
   * Initializes the touch event, resetting all paths and touch points.
   */
  fun onTouchInitialize()

  /**
   * Handles the start of a touch event.
   * Initializes the paths and stores the first touch point.
   *
   * @param offset The position of the touch start.
   * @param paint The paint object used for drawing the tape.
   */
  fun onTouchStart(
    offset: Offset = Offset.Zero,
    paint: Paint = Paint(),
  )

  /**
   * Handles movement during a touch event.
   * Updates the tape path and image path as the user moves their finger.
   *
   * @param previousOffset The previous position of the touch.
   * @param currentOffset The current position of the touch.
   * @param paint The paint object used for drawing the tape.
   */
  fun onTouchMove(
    previousOffset: Offset = Offset.Zero,
    currentOffset: Offset = Offset.Zero,
    paint: Paint = Paint(),
  )

  /**
   * Handles the end of a touch event.
   * If the touch was a tap, it selects the tape item at the current position.
   * Otherwise, it creates a new tape item and adds it to the controller.
   *
   * @param paint The paint object used for drawing the tape.
   */
  fun onTouchEnd(
    paint: Paint = Paint(),
  )

  /**
   * Draws the tape paths and image paths onto the canvas during the touch interaction.
   * This method is called continuously during the touch event to render the tape and image paths.
   *
   * @param drawScope The scope for drawing.
   * @param canvas The canvas on which to draw.
   * @param paint The paint object used for drawing the tape.
   * @param isMultiTouch A boolean indicating whether the touch event is multi-touch.
   */
  fun onDrawIntoCanvas(
    drawScope: DrawScope,
    canvas: Canvas,
    paint: Paint = Paint(),
    isMultiTouch: Boolean = false,
  )
}

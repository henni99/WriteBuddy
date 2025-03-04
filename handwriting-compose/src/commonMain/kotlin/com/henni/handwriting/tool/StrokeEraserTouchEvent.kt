package com.henni.handwriting.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import com.henni.handwriting.HandwritingController

/**
 * A class representing the touch event handling for the eraser tool. This class handles the touch interactions,
 *
 * @param controller The handwriting controller used to manage the eraser functionality.
 */

internal class StrokeEraserTouchEvent internal constructor(
  private val controller: HandwritingController,
) : ToolTouchEvent {

  // Stores the eraser path to mark areas to erase
  private var eraserPath by mutableStateOf(Path())

  // Stores the current offset (position) of the eraser
  private var currentOffset by mutableStateOf(Offset.Zero)

  override fun onTouchInitialize() {
    eraserPath = Path()
    currentOffset = Offset.Zero
  }

  override fun onTouchStart(
    canvas: Canvas?,
    offset: Offset,
    paint: Paint,
  ) {
    eraserPath = Path()
    currentOffset = offset
  }

  override fun onTouchMove(
    canvas: Canvas?,
    previousOffset: Offset,
    currentOffset: Offset,
    paint: Paint,
  ) {
    val radius = controller.eraserPointRadius

    this.currentOffset = currentOffset
    eraserPath.addOval(
      Rect(
        currentOffset.x - radius,
        currentOffset.y - radius,
        currentOffset.x + radius,
        currentOffset.y + radius,
      ),
    )
    controller.removeHandWritingPath(eraserPath)
  }

  override fun onTouchEnd(
    canvas: Canvas?,
    paint: Paint,
  ) {
    eraserPath = Path()
    currentOffset = Offset.Zero
  }

  override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint, isMultiTouch: Boolean) {
    if (!isMultiTouch && currentOffset != Offset.Zero && controller.isEraserPointShowed) {
      canvas.drawCircle(
        currentOffset,
        controller.eraserPointRadius,
        paint,
      )
    }
  }
}

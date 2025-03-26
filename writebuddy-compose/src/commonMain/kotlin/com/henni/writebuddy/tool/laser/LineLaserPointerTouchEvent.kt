package com.henni.writebuddy.tool.laser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.henni.writebuddy.extensions.setMaskFilter
import com.henni.writebuddy.model.copy
import com.henni.writebuddy.tool.ToolTouchEvent

/**
 * This class handles touch events related to the laser pointer tool, including initialization,
 * touch start, move, end, and drawing on the canvas. It manages the laser path and updates
 * the state of the laser pointer accordingly.
 *
 * @param controller The handwriting controller used to manage the laser paths.
 */

internal class LineLaserPointerTouchEvent internal constructor(
  private val controller: LaserPointerController,
) : ToolTouchEvent {

  /**
   * The path used to draw the laser pointer. Initialized to an empty path.
   */
  private var laserPath by mutableStateOf(Path())

  /**
   * Initializes the touch event, clearing the laser path.
   */
  override fun onTouchInitialize() {
    laserPath = Path()
  }

  /**
   * Handles the start of a touch event by creating a new laser path and adding it to the
   * controller's laser path list.
   *
   * @param offset The position where the touch event started.
   * @param paint The paint object used to draw the laser pointer.
   */
  override fun onTouchStart(offset: Offset, paint: Paint) = with(controller) {
    laserPath = Path()
    laserPath.moveTo(offset.x, offset.y)
    addLaserPath(laserPath)

    isLaserEnd.value = true
  }

  /**
   * Handles the movement of the touch event by updating the laser path with quadratic curves
   * between the previous and current touch positions.
   *
   * @param previousOffset The previous touch position.
   * @param currentOffset The current touch position.
   * @param paint The paint object used to draw the laser pointer.
   */
  override fun onTouchMove(
    previousOffset: Offset,
    currentOffset: Offset,
    paint: Paint,
  ) = with(controller) {
    laserPath.quadraticTo(
      previousOffset.x,
      previousOffset.y,
      (currentOffset.x + previousOffset.x) / 2,
      (currentOffset.y + previousOffset.y) / 2,
    )

    isLaserEnd.value = false
  }

  /**
   * Finalizes the touch event, marking the laser path as finished.
   *
   * @param paint The paint object used to draw the laser pointer.
   */
  override fun onTouchEnd(paint: Paint) = with(controller) {
    isLaserEnd.value = true
  }

  /**
   * Draws the laser path onto the canvas.
   *
   * @param drawScope The draw scope used to draw onto the canvas.
   * @param canvas The canvas on which to draw the laser pointer.
   * @param paint The paint object used to draw the laser pointer.
   * @param isMultiTouch A flag indicating if the touch event is in multi-touch state.
   */
  override fun onDrawIntoCanvas(
    drawScope: DrawScope,
    canvas: Canvas,
    paint: Paint,
    isMultiTouch: Boolean,
  ) = with(controller) {
    if (!isMultiTouch) {
      laserPathList.forEach { path ->

        canvas.drawOutline(
          outline = Outline.Generic(path),
          paint = Paint().copy(paint).apply {
            asFrameworkPaint().setMaskFilter(5f)
          },
        )

        canvas.drawPath(
          path,
          paint,
        )
      }
    }
  }
}

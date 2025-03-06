package com.henni.handwriting.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawOutline
import com.henni.handwriting.HandwritingController
import com.henni.handwriting.extensions.setMaskFilter
import com.henni.handwriting.model.copy

/**
 * A class representing the touch event handling for a laser pointer tool.
 * This class manages touch interactions, including initializing, updating, and rendering laser paths.
 *
 * @param controller The handwriting controller used to manage the laser paths.
 */

internal class LineLaserPointerTouchEvent internal constructor(
  private val controller: HandwritingController,
) : ToolTouchEvent {

  private var renderedLaserPath by mutableStateOf(Path())

  override fun onTouchInitialize() {
    renderedLaserPath = Path()
  }

  override fun onTouchStart(canvas: Canvas?, offset: Offset, paint: Paint) {
    renderedLaserPath = Path()
    renderedLaserPath.moveTo(offset.x, offset.y)
    controller.laserPathList.add(renderedLaserPath)

    controller.isLaserEnd = true
  }

  override fun onTouchMove(
    canvas: Canvas?,
    previousOffset: Offset,
    currentOffset: Offset,
    paint: Paint,
  ) {
    renderedLaserPath.quadraticTo(
      previousOffset.x,
      previousOffset.y,
      (currentOffset.x + previousOffset.x) / 2,
      (currentOffset.y + previousOffset.y) / 2,
    )

    controller.isLaserEnd = false
  }

  override fun onTouchEnd(canvas: Canvas?, paint: Paint) {
    controller.isLaserEnd = true
  }

  override fun onDrawIntoCanvas(canvas: Canvas, paint: Paint, isMultiTouch: Boolean) {
    if (!isMultiTouch) {
      controller.laserPathList.forEach { path ->

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

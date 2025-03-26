package com.henni.writebuddy.tool.tape

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.henni.writebuddy.extension.rotateMatrix
import com.henni.writebuddy.model.copy
import com.henni.writebuddy.tool.ToolTouchEvent
import kotlin.math.pow

/**
 * Handles touch events related to the tape tool on the canvas.
 * This class manages the creation and manipulation of tape paths and image paths
 * based on the user's touch input. It updates the state of the tape paths and interacts
 * with the controller to add new tape items when a touch interaction is completed.
 *
 * @param controller The controller responsible for managing tape items and interactions.
 */
internal class TapeTouchEvent internal constructor(
  private val controller: TapeController,
) : ToolTouchEvent {

  /** The path representing the tape's drawing on the canvas. */
  private var imagePath by mutableStateOf(Path())

  /** The path representing the actual tape's position and movement on the canvas. */
  private var tapePath by mutableStateOf(Path())

  /** The initial touch point where the tape drawing starts. */
  private var firstPoint by mutableStateOf(Offset.Zero)

  /** The last touch point as the user moves their finger. */
  private var lastPoint by mutableStateOf(Offset.Zero)

  /** A flag to determine whether the touch was a tap (no movement). */
  private var isTap = false

  /**
   * Initializes the touch event, resetting all paths and touch points.
   */
  override fun onTouchInitialize() {
    imagePath = Path()
    tapePath = Path()

    firstPoint = Offset.Zero
    lastPoint = Offset.Zero

    isTap = false
  }

  /**
   * Handles the start of a touch event.
   * Initializes the paths and stores the first touch point.
   *
   * @param offset The position of the touch start.
   * @param paint The paint object used for drawing the tape.
   */
  override fun onTouchStart(offset: Offset, paint: Paint) {
    isTap = true

    tapePath = Path()
    tapePath.moveTo(offset.x, offset.y)

    imagePath = Path()
    imagePath.moveTo(offset.x, offset.y)

    firstPoint = offset
    lastPoint = offset
  }

  /**
   * Handles movement during a touch event.
   * Updates the tape path and image path as the user moves their finger.
   *
   * @param previousOffset The previous position of the touch.
   * @param currentOffset The current position of the touch.
   * @param paint The paint object used for drawing the tape.
   */
  override fun onTouchMove(previousOffset: Offset, currentOffset: Offset, paint: Paint) {
    isTap = false

    tapePath = Path().apply {
      moveTo(firstPoint.x, firstPoint.y)
      lineTo(
        currentOffset.x,
        currentOffset.y,
      )
    }

    imagePath = Path().apply {
      moveTo(firstPoint.x, firstPoint.y)
      lineTo(
        currentOffset.x,
        currentOffset.y,
      )
    }

    lastPoint = currentOffset
  }

  /**
   * Handles the end of a touch event.
   * If the touch was a tap, it selects the tape item at the current position.
   * Otherwise, it creates a new tape item and adds it to the controller.
   *
   * @param paint The paint object used for drawing the tape.
   */
  override fun onTouchEnd(paint: Paint) = with(controller) {
    if (isTap) {
      selectTapeItem(lastPoint)
    } else {
      val tapeRect = RoundRect(
        left = firstPoint.x,
        top = firstPoint.y - paint.strokeWidth / 2,
        right = firstPoint.x + kotlin.math.sqrt(
          (lastPoint.x - firstPoint.x).pow(2) + (lastPoint.y - firstPoint.y).pow(
            2,
          ),
        ),
        bottom = firstPoint.y + paint.strokeWidth / 2,
        cornerRadius = cornerRadius,
      )

      tapePath = Path().apply {
        addRoundRect(tapeRect)
        transform(rotateMatrix(firstPoint, lastPoint))
      }
      tapePath = Path().apply {
        addPath(tapePath, offset = firstPoint)
      }

      controller.addTapeItem(
        TapeItem(
          bound = tapePath.getBounds(),
          startPoint = firstPoint,
          paint = Paint().copy(paint),
          imagePaint = Paint().copy(imagePaint),
          path = tapePath,
          imagePath = imagePath,
        ),
      )
    }

    tapePath = Path()
    imagePath = Path()
  }

  /**
   * Draws the tape paths and image paths onto the canvas during the touch interaction.
   * This method is called continuously during the touch event to render the tape and image paths.
   *
   * @param drawScope The scope for drawing.
   * @param canvas The canvas on which to draw.
   * @param paint The paint object used for drawing the tape.
   * @param isMultiTouch A boolean indicating whether the touch event is multi-touch.
   */
  override fun onDrawIntoCanvas(
    drawScope: DrawScope,
    canvas: Canvas,
    paint: Paint,
    isMultiTouch: Boolean,
  ) = with(controller) {
    if (!isMultiTouch) {
      tapeItemList.forEach { item ->

        item.path.translate(Offset(0f, 10f))

        if (selectedTapeItems.contains(item.id)) {
          canvas.drawPath(
            path = item.path,
            paint = Paint().apply {
              color = Color.LightGray
              alpha = 0.0f
              style = PaintingStyle.Fill
              strokeWidth = 0f
            },
          )
        } else {
          canvas.drawPath(
            path = item.path,
            paint = Paint().apply {
              color = Color.LightGray
              alpha = 1f
              style = PaintingStyle.Fill
              strokeWidth = 0f
            },
          )
        }

        item.path.translate(Offset(0f, -10f))

        if (selectedTapeItems.contains(item.id)) {
          canvas.drawPath(
            path = item.path,
            paint = item.paint.apply {
              alpha = selectedTapeAlpha
              style = PaintingStyle.Fill
              strokeWidth = 0f
            },
          )

          canvas.drawPath(
            path = item.imagePath,
            paint = item.imagePaint.apply {
              item.imagePaint.alpha = selectedTapeAlpha
            },
          )
        } else {
          canvas.drawPath(
            path = item.path,
            paint = item.paint.apply {
              alpha = 1f
              style = PaintingStyle.Fill
              strokeWidth = 0f
            },
          )
          canvas.drawPath(
            path = item.imagePath,
            paint = item.imagePaint.apply {
              alpha = 1f
              style = PaintingStyle.Fill
              strokeWidth = 0f
            },
          )
        }
      }

      paint.style = PaintingStyle.Stroke
      paint.color = paint.color
      canvas.drawPath(
        tapePath,
        paint,
      )

      canvas.drawPath(
        imagePath,
        imagePaint,
      )
    }
  }
}

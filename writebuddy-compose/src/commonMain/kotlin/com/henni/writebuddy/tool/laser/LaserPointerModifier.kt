package com.henni.writebuddy.tool.laser

import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.henni.writebuddy.extension.detectTransformGestures

/**
 * A modifier that enables laser pointer mode on a canvas, allowing for touch gestures such as
 * starting, moving, and ending a touch event with updates to the laser pointer’s appearance.
 *
 * @param controller The controller responsible for handling laser pointer actions.
 * @param progress A state that controls the progress (e.g., alpha transparency) of the laser pointer.
 * @param isEnabled A flag indicating whether the laser pointer mode is enabled.
 * @param onCanvasInvalidate A callback function invoked when the canvas needs to be invalidated.
 * @return The modifier applied to the canvas that handles the laser pointer gestures and drawing.
 */

fun Modifier.useLaserPointerMode(
  controller: LaserPointerController,
  progress: State<Float>,
  isEnabled: Boolean,
  onCanvasInvalidate: () -> Unit = { },
) = this then (
  if (isEnabled) {
    Modifier
      .pointerInput(Unit) {
        detectTransformGestures(
          onGestureStart = { offset ->
            controller.touchEvent.onTouchStart(
              offset = offset,
              paint = controller.paint,
            )

            controller.updateInvalidateTick()
          },
          onGesture = { change, isMultiTouch ->

            controller.updateIsMultiTouched(isMultiTouch)

            if (!isMultiTouch) {
              controller.touchEvent.onTouchMove(
                previousOffset = change.previousPosition,
                currentOffset = change.position,
                paint = controller.paint,
              )
            }

            controller.updateInvalidateTick()
          },
          onGestureEnd = { isMultiTouch ->

            controller.updateIsMultiTouched(isMultiTouch)

            if (!isMultiTouch) {
              controller.touchEvent.onTouchEnd(
                paint = controller.paint,
              )

              controller.updateInvalidateTick()
            }
          },
          onGestureCancel = {
            controller.touchEvent.onTouchInitialize()
            controller.updateInvalidateTick()
          },
        )
      }
  } else {
    Modifier
  }.drawBehind {
    drawIntoCanvas { canvas ->

      controller.touchEvent.onDrawIntoCanvas(
        drawScope = this@drawBehind,
        canvas = canvas,
        paint = controller.paint.apply {
          this.alpha = progress.value
        },
        isMultiTouch = controller.isMultiTouched,
      )

      if (controller.invalidateTick != 0) {
        onCanvasInvalidate()
      }
    }
  }
  )

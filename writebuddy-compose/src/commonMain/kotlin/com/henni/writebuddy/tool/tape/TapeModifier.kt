package com.henni.writebuddy.tool.tape

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.henni.writebuddy.extension.detectTransformGestures

/**
 * Modifier extension function to enable or disable tape mode on a canvas.
 *
 * This function allows users to interact with the canvas in tape mode, which includes
 * detecting gestures, handling touch events, and drawing the tape-related content on the canvas.
 *
 * @param controller The [TapeController] instance that manages the tape state and touch events.
 * @param isEnabled A boolean flag indicating whether tape mode should be enabled or disabled.
 * @param onCanvasInvalidate A callback function that will be invoked when the canvas needs to be invalidated.
 *
 * @return A modified [Modifier] that adds tape-related gesture handling and drawing functionality.
 */

fun Modifier.useTapeMode(
    controller: TapeController,
    isEnabled: Boolean,
    onCanvasInvalidate: () -> Unit = { }
) = this then (
        if (isEnabled) {
            Modifier
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGestureStart = { offset ->
                            controller.touchEvent.onTouchStart(
                                offset = offset,
                                paint = controller.paint
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
                        }
                    )
                }
        } else {
            Modifier
        }.drawBehind {

            drawIntoCanvas { canvas ->

                controller.touchEvent.onDrawIntoCanvas(
                    drawScope = this@drawBehind,
                    canvas = canvas,
                    paint = controller.paint,
                    isMultiTouch = controller.isMultiTouched,
                )

                if (controller.invalidateTick != 0) {
                    onCanvasInvalidate()
                }
            }
        }
        )

package com.henni.writebuddy.sticky

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import com.henni.writebuddy.Attachable
import com.henni.writebuddy.extension.detectTransformGestures

/**
 * A Composable that displays a sticky item which can be moved and zoomed using gestures.
 * This composable supports both dragging (panning) and pinch-to-zoom gestures.
 *
 * @param modifier Modifier to be applied to the sticky item.
 * @param attachable The attachable object (e.g., sticky item) to be displayed.
 * @param stickySize The size of the sticky item (used for positioning and layout).
 * @param onStickyMoved A callback that is triggered when the sticky item is moved.
 * @param onZoomChanged A callback that is triggered when the zoom level or zoom offset is changed.
 * @param content A composable that is displayed inside the sticky item.
 */

@Composable
fun Sticky(
    modifier: Modifier = Modifier,
    attachable: Attachable,
    stickySize: DpSize,
    onStickyMoved: (Offset) -> Unit,
    onZoomChanged: (Float, Offset) -> Unit,
    content: @Composable () -> Unit = {}
) {
    var isMultiTouched by remember { mutableStateOf(false) }

    var moveOffsetX by remember {
        mutableStateOf(
            attachable.firstPoint.x + attachable.translate.x
        )
    }

    var moveOffsetY by remember {
        mutableStateOf(
            attachable.firstPoint.y + attachable.translate.y
        )
    }

    var scaleFactor by remember { mutableStateOf(attachable.scaleFactor) }

    var scaleOffset by remember { mutableStateOf(attachable.scaleOffset) }

    Column(
        modifier = modifier
            .offset {
                IntOffset(
                    x = (-stickySize.width / 2).toPx().toInt(),
                    y = (-stickySize.height / 2).toPx().toInt()
                )
            }
            .offset {
                IntOffset(
                    x = moveOffsetX.toInt(),
                    y = moveOffsetY.toInt()
                )
            }
            .graphicsLayer {
                scaleX = scaleFactor
                scaleY = scaleFactor
                translationX = scaleOffset.x
                translationY = scaleOffset.y
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGestureStart = { _ -> },
                    onGesture = { zoomChange, panChange, change, isMultiTouch ->
                        isMultiTouched = isMultiTouch

                        if (isMultiTouch) {
                            scaleFactor = (scaleFactor * zoomChange).coerceIn(1f, 5f)

                            val extraWidth = (scaleFactor - 1) * size.width
                            val extraHeight = (scaleFactor - 1) * size.height

                            val maxX = extraWidth / 2
                            val maxY = extraHeight / 2

                            scaleOffset = Offset(
                                x = (scaleOffset.x + scaleFactor * panChange.x).coerceIn(
                                    -maxX,
                                    maxX
                                ),
                                y = (scaleOffset.y + scaleFactor * panChange.y).coerceIn(
                                    -maxY,
                                    maxY
                                ),
                            )
                        } else {
                            moveOffsetX += (change.position.x - change.previousPosition.x)
                            moveOffsetY += (change.position.y - change.previousPosition.y)
                        }
                    },
                    onGestureEnd = { isMultiTouch ->

                        if (isMultiTouch) {
                            onZoomChanged(scaleFactor, scaleOffset)
                        } else {
                            onStickyMoved(
                                Offset(
                                    x = moveOffsetX,
                                    y = moveOffsetY
                                )
                            )
                        }
                    },
                    onGestureCancel = { }
                )
            }
    ) {
        content()
    }
}


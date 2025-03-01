package com.henni.handwriting.kmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.henni.handwriting.kmp.extension.detectTransformGestures
import com.henni.handwriting.kmp.extension.getBitmap
import com.henni.handwriting.kmp.extension.updateTick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HandWritingNote(
    modifier: Modifier = Modifier,
    controller: HandwritingController,
    contentWidth: Dp,
    contentHeight: Dp
) {

    var touchPointerType by remember { mutableStateOf(PointerType.Touch) }

    var canvas: Canvas? by remember { mutableStateOf(null) }

    val invalidatorTick: MutableState<Int> = remember { mutableStateOf(0) }

    var pathBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    var canvasSize: IntSize by remember { mutableStateOf(IntSize.Zero) }

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    var multiTouch by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    SideEffect {
        coroutineScope.launch(Dispatchers.Main) {
            controller.refreshTick.collect {

                pathBitmap = getBitmap(canvasSize).also {
                    canvas = Canvas(it)
                }

                println("handwritingDataCollectionRevise: ${controller.handwritingPathCollection.size}")

                println("onSizeChanged: ${canvasSize} refreshTick")

                controller.handwritingPathCollection.forEach { data ->

                    if (!controller.isDataSelected(data)) {

                        canvas?.drawPath(
                            path = data.renderedPath,
                            paint = data.paint
                        )
                    }
                }

                invalidatorTick.updateTick()
            }
        }
    }

    Box(
        modifier = modifier
            .clipToBounds()
    ) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .width(contentWidth)
                .height(contentHeight)
                .onSizeChanged { newSize ->
                    val size =
                        newSize.takeIf { it.width != 0 && it.height != 0 } ?: return@onSizeChanged

                    println("onSizeChanged: ${size} onSizeChanged")

                    pathBitmap = getBitmap(size).also {
                        canvas = Canvas(it)
                    }
                }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y

                    canvasSize = IntSize(size.height.toInt(), size.width.toInt())
                }
                .clipToBounds()
                .align(Alignment.Center)
                .pointerInput(Unit) {

                    detectTransformGestures(
                        onGestureStart = { offset ->
                            println("detectDragGestures: onGestureStart ${offset}")

                            controller.currentTouchEvent.onTouchStart(
                                canvas = canvas,
                                offset = offset,
                                paint = controller.currentPaint
                            )

                            invalidatorTick.updateTick()
                        },
                        onGesture = { zoomChange: Float, panChange: Offset, change: PointerInputChange, isMultiTouch: Boolean ->
                            println("detectDragGestures: onGesture ${change.position} ${change.previousPosition}")
                            println("isMultiTouch :${isMultiTouch}")

                            multiTouch = isMultiTouch

                            if (isMultiTouch) {

                                scale = (scale * zoomChange).coerceIn(1f, 5f)

                                val extraWidth = (scale - 1) * canvasSize.width
                                val extraHeight = (scale - 1) * canvasSize.height

                                val maxX = extraWidth / 2
                                val maxY = extraHeight / 2

                                offset = Offset(
                                    x = (offset.x + scale * panChange.x).coerceIn(-maxX, maxX),
                                    y = (offset.y + scale * panChange.y).coerceIn(-maxY, maxY),
                                )

                            } else {

                                controller.currentTouchEvent.onTouchMove(
                                    canvas = canvas,
                                    previousOffset = change.previousPosition,
                                    currentOffset = change.position,
                                    paint = controller.currentPaint
                                )

                            }

                            invalidatorTick.updateTick()


                        },
                        onGestureEnd = { isMultiTouch ->

                            println("detectDragGestures: onDragEnd ${isMultiTouch}")
                            multiTouch = isMultiTouch
                            if (!isMultiTouch) {

                                controller.currentTouchEvent.onTouchEnd(
                                    canvas = canvas,
                                    paint = controller.currentPaint
                                )

                                invalidatorTick.updateTick()
                            }

                        },
                        onGestureCancel = {
                            controller.currentTouchEvent.onTouchCancel()
                            invalidatorTick.updateTick()
                        }
                    )
                }
        ) {
            drawIntoCanvas { canvas ->
                drawRect(controller.contentBackground)

                // draw path bitmap on the canvas.
                pathBitmap?.let { bitmap ->
                    canvas.drawImage(bitmap, Offset.Zero, Paint())
                }

                controller.currentTouchEvent.onDrawIntoCanvas(
                    canvas = canvas,
                    paint = controller.currentPaint,
                    isMultiTouch = multiTouch
                )

            }


            println("currentMode: ${controller.currentTouchEvent}")
            println("selectedDataSet: ${controller.selectedDataSet.size}")
            if (invalidatorTick.value != 0) {
//            onRevisedListener?.invoke(controller.canUndo.value, controller.canRedo.value)
            }
        }
    }
}
package com.anonymous.handwriting

import android.graphics.Point
import android.graphics.PointF
import android.graphics.PorterDuff
import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.core.util.Pools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HandWritingNote(
    modifier: Modifier = Modifier,
    controller: HandwritingState
) {

//    var path: Path by remember { mutableStateOf(Path()) }
//    var canvas: Canvas? by remember { mutableStateOf(null) }

    var penPath by remember { mutableStateOf(Path()) }
    var penPathPoints: MutableList<Point> by remember { mutableStateOf(mutableListOf()) }
    var eraserPath by remember { mutableStateOf(Path()) }
    var lassoPath by remember { mutableStateOf(Path()) }
    var canvas: Canvas? by remember { mutableStateOf(null) }
    var currentPoint: PointF by remember { mutableStateOf(PointF(0f, 0f)) }
    val touchTolerance = LocalViewConfiguration.current.touchSlop
    val invalidatorTick: MutableState<Int> = remember { mutableStateOf(0) }
    var pathBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    var firstPoint: PointF by remember { mutableStateOf(PointF(0f, 0f)) }
    var lassoMoveFlag: Boolean by remember { mutableStateOf(false) }
    var lassoMoveFirstFlag: Boolean by remember { mutableStateOf(false) }
    var transformMatrix: Matrix by remember { mutableStateOf(Matrix()) }


    var tmpList: MutableList<com.anonymous.handwriting.Point> by remember {
        mutableStateOf(
            mutableListOf()
        )
    }
//    DisposableEffect(key1 = controller) {
//        controller.setImageBitmap(imageBitmap)
//        controller.setBackgroundColor(backgroundColor)
//
//        onDispose {
//            controller.releaseBitmap()
//            controller.clear()
//        }
//    }

    val coroutineScope = rememberCoroutineScope()
    SideEffect {
        coroutineScope.launch(Dispatchers.Main) {
            controller.reviseTick.collect {
                Log.d(
                    "reviseTick",
                    "${it} "
                )

                Log.d(
                    "reviseTick - canUndo",
                    "${controller.canUndo.value} ${controller.canRedo.value}"
                )
                Log.d(
                    "reviseTick - handwritingElements",
                    controller.handwritingElements.size.toString()
                )

                canvas?.nativeCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)


                controller.handwritingElements.forEach { element ->

                    Log.d("handwritingElements", element.matrix.toString())

                    if (!controller.selectedElements.value.contains(element)) {
                        canvas?.drawPath(element.path, defaultPaint())
                    }


//                    canvas?.let {
//                        element.drawToCanvas(it, transformMatrix)
//                    }

                }

                invalidatorTick.value ++
//                canvas?.drawRect(controller.selectedBoundBox.value, lassoLinePaint())

//                Log.d("selectedBoundBox", controller.selectedBoundBox.toString())

//                controller.selectedBoundBox.value.translate(transformMatrix.values[12], transformMatrix.values[13])
//                canvas?.drawRect(controller.selectedBoundBox.value, lassoLinePaint())

            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { newSize ->
                val size =
                    newSize.takeIf { it.width != 0 && it.height != 0 } ?: return@onSizeChanged
                pathBitmap =
                    ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
                        .also {
                            canvas = Canvas(it)
                        }

                Log.d("pathBitmap", pathBitmap.toString())
            }
            .pointerInteropFilter { event ->

                val motionTouchEventX = event.x
                val motionTouchEventY = event.y

                when (controller.currentMode.value) {
                    HandWritingMode.PEN -> {

                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                tmpList.add(
                                    com.anonymous.handwriting.Point(
                                        motionTouchEventX.toInt(),
                                        motionTouchEventY.toInt()
                                    )
                                )
                                penPath.reset()
                                penPath.moveTo(motionTouchEventX, motionTouchEventY)
                                currentPoint.set(motionTouchEventX, motionTouchEventY)
                            }

                            MotionEvent.ACTION_MOVE -> {
                                tmpList.add(
                                    com.anonymous.handwriting.Point(
                                        motionTouchEventX.toInt(),
                                        motionTouchEventY.toInt()
                                    )
                                )

                                penPath.lineTo(
                                    motionTouchEventX,
                                    motionTouchEventY,
                                )

                                canvas?.drawPath(penPath, controller.currentPaint.value)
                            }

                            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                                tmpList.add(
                                    com.anonymous.handwriting.Point(
                                        motionTouchEventX.toInt(),
                                        motionTouchEventY.toInt()
                                    )
                                )
                                Log.d("tmpList", tmpList.size.toString())
                                controller.addHandWritingPath(penPath, tmpList)
                                tmpList = mutableListOf()
                                penPath = Path()
                                currentPoint.set(-40f, -40f)
                            }
                        }
                    }

                    HandWritingMode.ERASER -> {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                currentPoint.set(motionTouchEventX, motionTouchEventY)
                            }

                            MotionEvent.ACTION_MOVE -> {
                                currentPoint.set(motionTouchEventX, motionTouchEventY)
                                eraserPath.addOval(
                                    Rect(
                                        motionTouchEventX - 10,
                                        motionTouchEventY - 10,
                                        motionTouchEventX + 10,
                                        motionTouchEventY + 10
                                    )
                                )

                                controller.removeHandWritingPath(
                                    eraserPath, motionTouchEventX.toInt(), motionTouchEventY.toInt()
                                )
                            }

                            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                                currentPoint.set(-40f, -40f)
                            }
                        }
                    }

                    HandWritingMode.LASSO_SELECTION -> {

                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                lassoPath.reset()
                                tmpList.add(
                                    com.anonymous.handwriting.Point(
                                        motionTouchEventX.toInt(),
                                        motionTouchEventY.toInt()
                                    )
                                )
                                lassoPath.moveTo(motionTouchEventX, motionTouchEventY)
                                currentPoint.set(motionTouchEventX, motionTouchEventY)
                            }

                            MotionEvent.ACTION_MOVE -> {
                                lassoPath.lineTo(
                                    motionTouchEventX,
                                    motionTouchEventY,
                                )
                                tmpList.add(
                                    com.anonymous.handwriting.Point(
                                        motionTouchEventX.toInt(),
                                        motionTouchEventY.toInt()
                                    )
                                )

                            }

                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                tmpList.add(
                                    com.anonymous.handwriting.Point(
                                        motionTouchEventX.toInt(),
                                        motionTouchEventY.toInt()
                                    )
                                )
                                controller.selectHandWritingElements(lassoPath)
                                tmpList = mutableListOf()
                                lassoPath = Path()
                            }
                        }
                    }

                    HandWritingMode.LASSO_MOVE -> {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                lassoPath.reset()
                                lassoPath.moveTo(motionTouchEventX, motionTouchEventY)


                                if (controller.selectedBoundBox.value.contains(
                                        Offset(
                                            motionTouchEventX,
                                            motionTouchEventY
                                        )
                                    )
                                ) {
                                    lassoMoveFlag = true
                                    lassoMoveFirstFlag = true

                                    controller.reviseTick.update { it + 1 }
                                    currentPoint.set(motionTouchEventX, motionTouchEventY)
                                    firstPoint.set(motionTouchEventX, motionTouchEventY)
                                    Log.d("ACTION_DOWN", "LASSO_MOVE")
                                } else {

                                    controller.setCurrentMode(HandWritingMode.LASSO_SELECTION)
                                    controller.selectedElements.value = emptySet()
                                    controller.selectedBoundBox.value = Rect.Zero
                                }
                            }

                            MotionEvent.ACTION_MOVE -> {
                                if (lassoMoveFlag) {

                                    transformMatrix.reset()
                                    transformMatrix.translate(
                                        motionTouchEventX - currentPoint.x,
                                        motionTouchEventY - currentPoint.y
                                    )

                                    controller.selectedBoundBox.value =
                                        controller.selectedBoundBox.value.translate(
                                            transformMatrix.values[12],
                                            transformMatrix.values[13]
                                        )

                                    currentPoint.set(motionTouchEventX, motionTouchEventY)

                                    if (lassoMoveFirstFlag) {
                                        lassoMoveFirstFlag = false
                                        controller.reviseTick.update { it + 1 }
                                    }
                                }
                            }

                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                if (lassoMoveFlag) {

                                    transformMatrix.reset()
                                    transformMatrix.translate(
                                        motionTouchEventX - currentPoint.x,
                                        motionTouchEventY - currentPoint.y
                                    )

                                    controller.selectedBoundBox.value =
                                        controller.selectedBoundBox.value.translate(
                                            transformMatrix.values[12],
                                            transformMatrix.values[13]
                                        )

                                    Log.d("translateOffset", "${motionTouchEventX} ${motionTouchEventY} ${firstPoint}")

                                    controller.transformHandWritings(
                                        translateOffset = Offset(
                                            x = motionTouchEventX - firstPoint.x,
                                            y = motionTouchEventY - firstPoint.y
                                        )
                                    )

                                     firstPoint.set(0f, 0f)

                                    currentPoint.set(motionTouchEventX, motionTouchEventY)
                                    controller.reviseTick.update { it + 1 }
                                }
                            }
                        }
                    }

                    else -> {
                        return@pointerInteropFilter false
                    }
                }


                invalidatorTick.value++
                return@pointerInteropFilter true
            }

    ) {
        drawIntoCanvas { canvas ->

            // draw image bitmap on the canvas.
//            controller.imageBitmap?.let { imageBitmap ->
//                var dx = 0f
//                var dy = 0f
//                val scale: Float
//                val shaderMatrix = Matrix()
//                val shader = ImageShader(imageBitmap, TileMode.Clamp)
//                val brush = ShaderBrush(shader)
//                val paint = paintPool.acquire() ?: Paint()
//                paint.asFrameworkPaint().apply {
//                    isAntiAlias = true
//                    isDither = true
//                    isFilterBitmap = true
//                }
//
//                Log.d("drawIntoCanvas", "${canvas.nativeCanvas}")
//
//                // cache the paint in the internal stack.
//                canvas.saveLayer(size.toRect(), paint)
//
//                val mDrawableRect = RectF(0f, 0f, size.width, size.height)
//                val bitmapWidth: Int = imageBitmap.asAndroidBitmap().width
//                val bitmapHeight: Int = imageBitmap.asAndroidBitmap().height
//
//                if (bitmapWidth * mDrawableRect.height() > mDrawableRect.width() * bitmapHeight) {
//                    scale = mDrawableRect.height() / bitmapHeight.toFloat()
//                    dx = (mDrawableRect.width() - bitmapWidth * scale) * 0.5f
//                } else {
//                    scale = mDrawableRect.width() / bitmapWidth.toFloat()
//                    dy = (mDrawableRect.height() - bitmapHeight * scale) * 0.5f
//                }
//
//                // resize the matrix to scale by sx and sy.
//                shaderMatrix.setScale(scale, scale)
//
//                // post translate the matrix with the specified translation.
//                shaderMatrix.postTranslate(
//                    (dx + 0.5f) + mDrawableRect.left,
//                    (dy + 0.5f) + mDrawableRect.top
//                )
//                // apply the scaled matrix to the shader.
//                shader.setLocalMatrix(shaderMatrix)
//                // Set the shader matrix to the controller.
//                controller.imageBitmapMatrix.value = shaderMatrix
//                // draw an image bitmap as a rect.
//                drawRect(brush = brush, size = controller.bitmapSize.value.toSize())
//                // restore canvas
//                canvas.restore()
//                // resets the paint and release to the pool.
//                paint.asFrameworkPaint().reset()
//                paintPool.release(paint)
//            }

            // draw path bitmap on the canvas.
            pathBitmap?.let { bitmap ->
                canvas.drawImage(bitmap, Offset.Zero, Paint())
            }

            when (controller.currentMode.value) {
                HandWritingMode.ERASER -> {
                    canvas.drawCircle(
                        Offset(currentPoint.x ?: -40f, currentPoint.y ?: -40f),
                        20f,
                        controller.currentPaint.value
                    )
                }

                HandWritingMode.LASSO_SELECTION -> {
                    canvas.drawPath(lassoPath, controller.currentPaint.value)
                }

                HandWritingMode.LASSO_MOVE -> {
                    canvas.drawRect(
                        controller.selectedBoundBox.value,
                        lassoLinePaint()
                    )
                    controller.selectedElements.value.forEach { element ->
                        element.path.transform(transformMatrix)
                        canvas.drawPath(element.path, defaultPaint())
                    }
                }

                else -> {

                }

            }


//            if(controller.currentMode.value == HandWritingMode.LASSO_MOVE) {
//                canvas.drawRect(controller.selectedBoundBox.value, lassoLinePaint())

//                }
//            }
        }
        Log.d("mode", controller.currentMode.toString())

        Log.d("controller", invalidatorTick.toString())
        Log.d(
            "invalidateTick - canUndo",
            "${controller.canUndo.value} ${controller.canRedo.value}"
        )

        if (invalidatorTick.value != 0) {
//            onRevisedListener?.invoke(controller.canUndo.value, controller.canRedo.value)
        }
    }

}

private
val paintPool = Pools.SimplePool<Paint>(2)

package com.anonymous.handwriting

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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.core.util.Pools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HandWritingNote(
    modifier: Modifier = Modifier,
    controller: HandwritingState
) {

//    var path: Path by remember { mutableStateOf(Path()) }
//    var canvas: Canvas? by remember { mutableStateOf(null) }

    var path by remember { mutableStateOf(Path()) }
    var eraserPath by remember { mutableStateOf(Path()) }
    var canvas: Canvas? by remember { mutableStateOf(null) }
    var currentPoint: PointF? by remember { mutableStateOf(null) }
    val touchTolerance = LocalViewConfiguration.current.touchSlop
    val invalidatorTick: MutableState<Int> = remember { mutableStateOf(0) }
    var pathBitmap: ImageBitmap? by remember { mutableStateOf(null) }
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
                    "reviseTick - canUndo",
                    "${controller.canUndo.value} ${controller.canRedo.value}"
                )
                Log.d(
                    "reviseTick - handwritingElements",
                    controller.handwritingElements.size.toString()
                )

                canvas?.nativeCanvas?.drawColor(0, PorterDuff.Mode.CLEAR)
                controller.handwritingElements.forEach { element ->
                    canvas?.drawPath(element.path, element.paint)
                }


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

                Log.d("pointerInteropFilter", event.toString())

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.reset()
                        path.moveTo(motionTouchEventX, motionTouchEventY)
                        currentPoint = PointF(motionTouchEventX, motionTouchEventY)

//                        if (controller.currentMode.value == HandWritingMode.ERASER) {
//                            controller.removeHandWritingPath(
//                                motionTouchEventX.toInt(),
//                                motionTouchEventY.toInt()
//                            )
//                        }
                    }

                    MotionEvent.ACTION_MOVE -> {

                        path.lineTo(
                            motionTouchEventX,
                            motionTouchEventY,
                        )
                        currentPoint = PointF(motionTouchEventX, motionTouchEventY)

                        if (controller.currentMode.value == HandWritingMode.ERASER) {
//                            eraserPath.reset()
                            eraserPath.addOval(
                                Rect(
                                    motionTouchEventX - 10,
                                    motionTouchEventY - 10,
                                    motionTouchEventX + 10,
                                    motionTouchEventY + 10
                                )
                            )
                            controller.removeHandWritingPath(eraserPath, motionTouchEventX.toInt(), motionTouchEventY.toInt())


                        } else {
                            canvas?.drawPath(path, controller.currentPaint.value)
                        }

                    }

                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {

//                        onPathListener?.invoke(path)
                        Log.d("currentMode", controller.currentMode.toString())
                        if (controller.currentMode.value == HandWritingMode.PEN) {
                            controller.addHandWritingPath(path)
                            path = Path()
                        } else if (controller.currentMode.value == HandWritingMode.ERASER) {

                        }

                        currentPoint = null
                    }

                    else -> false
                }
//                onEventListener?.invoke(event.x, event.y, event.action)
                invalidatorTick.value++
                true
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

            if (controller.currentMode.value == HandWritingMode.ERASER) {

                canvas.drawCircle(
                    Offset(currentPoint?.x ?: -40f, currentPoint?.y ?: -40f),
                    20f,
                    controller.currentPaint.value
                )
            }
        }
        Log.d("mode", controller.currentMode.toString())

        Log.d("controller", invalidatorTick.toString())
        Log.d("reviseTick - canUndo", "${controller.canUndo.value} ${controller.canRedo.value}")

        if (invalidatorTick.value != 0) {
//            onRevisedListener?.invoke(controller.canUndo.value, controller.canRedo.value)
        }
    }

}

private val paintPool = Pools.SimplePool<Paint>(2)

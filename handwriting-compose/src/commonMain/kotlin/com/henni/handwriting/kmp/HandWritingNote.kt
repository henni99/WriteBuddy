package com.henni.handwriting.kmp

import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import com.henni.handwriting.kmp.model.ToolMode
import com.henni.handwriting.kmp.model.defaultPaint
import com.henni.handwriting.kmp.model.defaultPaint2
import com.henni.handwriting.kmp.model.lassoDefaultPaint
import com.henni.handwriting.kmp.tool.onEraserDrag
import com.henni.handwriting.kmp.tool.onEraserDragEnd
import com.henni.handwriting.kmp.tool.onEraserDragStart
import com.henni.handwriting.kmp.tool.onLassoMoveDragStart
import com.henni.handwriting.kmp.tool.onLassoSelectDrag
import com.henni.handwriting.kmp.tool.onLassoSelectDragCancel
import com.henni.handwriting.kmp.tool.onLassoSelectDragEnd
import com.henni.handwriting.kmp.tool.onLassoSelectDragStart
import com.henni.handwriting.kmp.tool.onLassoTap
import com.henni.handwriting.kmp.tool.onPenDrag
import com.henni.handwriting.kmp.tool.onPenDragCancel
import com.henni.handwriting.kmp.tool.onPenDragEnd
import com.henni.handwriting.kmp.tool.onPenDragStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HandWritingNote(
    modifier: Modifier = Modifier,
    controller: HandwritingController,
    eraserPointRadius: Int = 10,
) {

    var penPath by remember { mutableStateOf(Path()) }
    val penPathOffsets = mutableStateListOf<Offset>()

    var eraserPath by remember { mutableStateOf(Path()) }

    var lassoPath by remember { mutableStateOf(Path()) }
    val lassoPathOffsets = mutableStateListOf<Offset>()
    var isLassoTap by remember { mutableStateOf(true) }
    var currentPoint by remember { mutableStateOf(Offset.Zero) }

    var canvas: Canvas? by remember { mutableStateOf(null) }

    val invalidatorTick: MutableState<Int> = remember { mutableStateOf(0) }

    var pathBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    var firstPoint: Offset by remember { mutableStateOf(Offset.Zero) }
    var lassoMoveFlag: Boolean by remember { mutableStateOf(false) }
    var lassoMoveFirstFlag: Boolean by remember { mutableStateOf(false) }
    var transformMatrix: Matrix by remember { mutableStateOf(Matrix()) }

    var canvasSize: Size by remember { mutableStateOf(Size(0f, 0f)) }


    var currentOffset: Offset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val coroutineScope = rememberCoroutineScope()

    SideEffect {
        coroutineScope.launch(Dispatchers.Main) {
            controller.refreshTick.collect {


                canvasSize.let {
                    pathBitmap =
                        ImageBitmap(it.width.toInt(), it.height.toInt(), ImageBitmapConfig.Argb8888)
                            .also {
                                canvas = Canvas(it)
                            }
                }

                println("handwritingDataCollectionRevise: ${controller.selectedDataSet.size}")


                controller.handwritingDataCollection.forEach { data ->

                    if (!controller.isDataSelected(data)) {
                        canvas?.drawPath(
                            path = data.path,
                            paint = defaultPaint()
                        )
                    }
                }

                invalidatorTick.updateTick()
            }
        }
    }


    var fingerCount by remember { mutableStateOf(0) }

    androidx.compose.foundation.Canvas(
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

            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y

                canvasSize = size
            }
            .pointerInput(true) {
                detectTapGestures { offset ->

                    when (controller.activeToolMode.value) {
                        ToolMode.LassoSelectMode, ToolMode.LassoMoveMode -> {
                            onLassoTap(
                                currentOffset = offset,
                                onTapEnd = { path ->
                                    controller.initializeSelection()
                                    controller.selectHandWritingData(
                                        path = path,
                                    )
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->

                        if(fingerCount == 1) {
                            when (controller.activeToolMode.value) {
                                ToolMode.PenMode -> {
                                    onPenDragStart(
                                        canvas = canvas,
                                        offset = offset,
                                        penPathOffsets = penPathOffsets,
                                        paint = controller.activePaint.value,
                                        penPath = penPath
                                    )
                                }

                                ToolMode.EraserMode -> {
                                    onEraserDragStart(
                                        currentOffset = currentOffset,
                                        offset = offset
                                    )
                                }

                                ToolMode.LassoSelectMode -> {
                                    onLassoSelectDragStart(
                                        canvas = canvas,
                                        offset = offset,
                                        lassoPathOffsets = lassoPathOffsets,
                                        paint = controller.activePaint.value,
                                        lassoPath = lassoPath
                                    )

                                    controller.refreshTick.updateTick()
                                }

                                ToolMode.LassoMoveMode -> {
                                    onLassoMoveDragStart(
                                        offset = offset,
                                        lassoPath = lassoPath,
                                        selectedBounds = controller.selectedBoundBox.value,
                                        isMoveAllowed = { path ->
                                            lassoMoveFlag = true
                                            lassoMoveFirstFlag = true
                                            currentPoint = offset
                                            firstPoint = offset

                                            controller.refreshTick.updateTick()
                                        },
                                        isMoveNotAllowed = {
                                            controller.updateToolMode(ToolMode.LassoSelectMode)

                                        }
                                    )
                                }

                                else -> {

                                }
                            }

                            invalidatorTick.updateTick()
                        }
                    },
                    onDrag = { change, dragAmount ->
                        if(fingerCount == 1) {
                            when (controller.activeToolMode.value) {
                                ToolMode.PenMode -> {

                                    onPenDrag(
                                        canvas = canvas,
                                        previousOffset = change.previousPosition,
                                        currentOffset = change.position,
                                        penPathOffsets = penPathOffsets,
                                        paint = controller.activePaint.value,
                                        penPath = penPath
                                    )

                                }

                                ToolMode.EraserMode -> {

                                    onEraserDrag(
                                        eraserPath = eraserPath,
                                        eraserPointRadius = eraserPointRadius,
                                        currentOffset = currentOffset,
                                        onPathRemoved = { path ->
                                            currentOffset = change.position
                                            controller.removeHandWritingPath(path)
                                        }
                                    )
                                }

                                ToolMode.LassoSelectMode -> {
                                    onLassoSelectDrag(
                                        onDragPrevious = { _ ->
                                            isLassoTap = false
                                        },
                                        canvas = canvas,
                                        previousOffset = change.previousPosition,
                                        currentOffset = change.position,
                                        lassoPathOffsets = lassoPathOffsets,
                                        paint = controller.activePaint.value,
                                        lassoPath = lassoPath
                                    )
                                }

                                ToolMode.LassoMoveMode -> {
                                    if (lassoMoveFlag) {

                                        transformMatrix.reset()
                                        transformMatrix.translate(
                                            dragAmount.x,
                                            dragAmount.y
                                        )

                                        currentPoint = change.position

                                        println("transformMatrix: transformMatrixInLassoMoveMode: ${transformMatrix}")
                                        controller.transformSelectedBoundBox(transformMatrix)
                                        controller.selectedDataSet.forEach { data ->
                                            data.path.transform(transformMatrix)
                                        }
                                    }
                                }

                                else -> {

                                }
                            }

                            invalidatorTick.updateTick()
                        }
                    },
                    onDragEnd = {
                        if(fingerCount == 1) {
                            when (controller.activeToolMode.value) {
                                ToolMode.PenMode -> {
                                    onPenDragEnd(
                                        canvas = canvas,
                                        paint = controller.activePaint.value,
                                        penPathOffsets = penPathOffsets,
                                        penPath = penPath,
                                        onDragFinished = { path, offsets ->
                                            controller.addHandWritingPath(path, offsets)
                                            penPath = Path()
                                        }
                                    )
                                }

                                ToolMode.EraserMode -> {
                                    onEraserDragEnd(
                                        eraserPath = eraserPath,
                                        onDragFinished = {
                                            currentOffset = Offset.Zero
                                        }
                                    )
                                }

                                ToolMode.LassoSelectMode -> {
                                    onLassoSelectDragEnd(
                                        lassoPathOffsets = lassoPathOffsets,
                                        lassoPath = lassoPath,
                                        onDragFinished = { path, offsets ->
                                            controller.selectHandWritingData(
                                                path = path,
                                            )
                                            lassoPath = Path()
                                            offsets.clear()
                                        }
                                    )
                                }

                                ToolMode.LassoMoveMode -> {
                                    if (lassoMoveFlag) {

                                        println("translateHandWritingDataSet: ${firstPoint} ${currentPoint}")

                                    }
                                    controller.translateHandWritingDataSet(
                                        translateOffset = Offset(
                                            x = currentPoint.x - firstPoint.x,
                                            y = currentPoint.y - firstPoint.y
                                        )
                                    )

                                    firstPoint = Offset.Zero
                                    currentPoint = Offset.Zero
                                    controller.refreshTick.updateTick()
                                }

                                else -> {

                                }
                            }

                            invalidatorTick.updateTick()
                        }
                    },
                    onDragCancel = {
                        when (controller.activeToolMode.value) {
                            ToolMode.PenMode -> {
                                onPenDragCancel(
                                    penPath = penPath,
                                    penPathOffsets = penPathOffsets
                                )
                            }

                            ToolMode.LassoSelectMode -> {
                                onLassoSelectDragCancel(
                                    lassoPath = lassoPath,
                                    lassoPathOffsets = lassoPathOffsets
                                )
                            }

                            ToolMode.LassoMoveMode -> {
                                controller.initializeSelection()
                                firstPoint = Offset.Zero
                                currentPoint = Offset.Zero
                            }

                            else -> {

                            }
                        }

                        invalidatorTick.updateTick()
                    }
                )
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val pointers = event.changes.count()


                        if (pointers >= 2) {
                            fingerCount = 2

                            val zoomChange = event.calculateZoom()
                            val panChange = event.calculatePan()

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
                            fingerCount = 1
                        }
                    }
                }
            }
    ) {
        drawIntoCanvas { canvas ->

            // draw path bitmap on the canvas.
            pathBitmap?.let { bitmap ->
                canvas.drawImage(bitmap, Offset.Zero, Paint())
            }

            when (controller.activeToolMode.value) {
                ToolMode.EraserMode -> {

                    println("currentOffset: ${currentOffset}")

                    if (currentOffset != Offset.Zero) {
                        canvas.drawCircle(
                            currentOffset,
                            eraserPointRadius * 2f,
                            controller.activePaint.value
                        )
                    }
                }

                ToolMode.LassoSelectMode -> {
                    canvas.drawPath(lassoPath, controller.activePaint.value)

                    controller.handwritingDataCollection.forEach { data ->

                        val dataPath = Path().apply {
                            addPath(data.path)
                        }

                        val pathWithOp = Path().apply {
                            this.op(dataPath, lassoPath, PathOperation.Intersect)
                        }
                        canvas.drawPath(pathWithOp, defaultPaint2())
                    }
                }

                ToolMode.LassoMoveMode -> {


                    canvas.drawRect(
                        controller.selectedBoundBox.value,
                        lassoDefaultPaint()
                    )

                    println("transformMatrix: transformMatrix: ${transformMatrix}")

                    controller.selectedDataSet.forEach { data ->
                        canvas.drawPath(data.path, defaultPaint())
                    }
                }

                else -> {

                }

            }



        }

        println("selectedDataSet: ${controller.selectedDataSet.size}")
        if (invalidatorTick.value != 0) {
//            onRevisedListener?.invoke(controller.canUndo.value, controller.canRedo.value)
        }
    }
}
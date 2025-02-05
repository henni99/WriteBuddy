package com.anonymous.handwriting

import android.graphics.Canvas
import android.graphics.Region
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.core.graphics.toRect
import com.anonymous.handwriting.operation.InsertOperation
import com.anonymous.handwriting.operation.OperationManager
import com.anonymous.handwriting.operation.OperationManagerImpl
import com.anonymous.handwriting.operation.RemoveOperation
import com.anonymous.handwriting.operation.TranslateOperation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Composable
public fun rememberHandwritingState(): HandwritingState {
    return remember { HandwritingState() }
}


@Stable
class HandwritingState internal constructor(
    private val operationManager: OperationManager = OperationManagerImpl()
) {
    val reviseTick = MutableStateFlow<Int>(0)

    val currentMode: MutableState<HandWritingMode> = mutableStateOf(HandWritingMode.PEN)

    var currentPaint: MutableState<Paint> = mutableStateOf(defaultPaint())


    fun setCurrentPaint(paint: Paint) {
        this.currentPaint.value = paint
    }


    fun setCurrentMode(handWritingMode: HandWritingMode) {
        currentMode.value = handWritingMode
        selectedBoundBox.value = Rect.Zero


        when (handWritingMode) {
            HandWritingMode.PEN -> {
                currentPaint.value = defaultPaint()
            }

            HandWritingMode.ERASER -> {
                currentPaint.value = defaultPaint()
            }

            HandWritingMode.LASSO_SELECTION -> {
                currentPaint.value = lassoLinePaint()
            }

            else -> {}
        }

        reviseTick.update { it + 1 }
    }


    /** undo, redo **/
    val canUndo = MutableStateFlow<Boolean>(false)

    val canRedo = MutableStateFlow<Boolean>(false)


    fun updateOperationStack() {
        canUndo.value = operationManager.undoOperationStack.isNotEmpty()
        canRedo.value = operationManager.redoOperationStack.isNotEmpty()
    }

    fun undo() {
        operationManager.undo()
        reviseTick.update { it + 1 }
        updateOperationStack()
    }

    fun redo() {
        operationManager.redo()
        reviseTick.update { it + 1 }
        updateOperationStack()
    }


    val handwritingElements = ArrayDeque<HandWritingElement>()

    val selectedElements: MutableState<Set<HandWritingElement>> = mutableStateOf(emptySet())

    val selectedBoundBox: MutableState<Rect> = mutableStateOf(Rect.Zero)

    fun addHandWritingPath(path: Path, tmpList: MutableList<Point>) {
        Log.d("addHandWritingPath", operationManager.toString())

        Log.d("addHandWritingPath", System.currentTimeMillis().toString())


        operationManager.executeOperation(
            InsertOperation(
                handwritingState = this@HandwritingState,
                element = HandWritingElement(
                    path = path,
                    originalPoints = tmpList,
                    paint = currentPaint.value,
                )
            ),

            )
        Log.d("addHandWritingPath", System.currentTimeMillis().toString())

//        handwritingElements.add(
//            HandWritingElement(
//                path = path,
//                paint = currentPaint.value,
//            )
//        )
    }


    fun removeHandWritingPath(path: Path) {

        val hitResult = hitHandWritingPath(path)
        val handwritingElement = hitResult.first
        val isHitHandWritingPath = hitResult.second

        Log.d("hitResult", "${hitResult} ${handwritingElements.size}".toString())

        if (isHitHandWritingPath) {

            handwritingElement?.let {

                operationManager.executeOperation(
                    RemoveOperation(
                        handwritingState = this@HandwritingState,
                        element = it
                    )
                )
            }
        }
    }

    fun transformHandWritings(
        translateOffset: Offset
    ) {
        operationManager.executeOperation(
            TranslateOperation(
                handwritingState = this@HandwritingState,
                elements = selectedElements.value,
                translateOffset = translateOffset
            )
        )


    }

    fun selectHandWritingElements(path: Path, canvas: androidx.compose.ui.graphics.Canvas?) {

        val tempSelectedElements = mutableSetOf<HandWritingElement>()
        var tempRect = Rect.Zero

        val lassoRegion = createRegionFromPath(path)
        Log.d("isIntersectionisIntersection", handwritingElements.size.toString())

        var bef = System.currentTimeMillis()
        for (idx in handwritingElements.size - 1 downTo 0) {
            val element = handwritingElements[idx]
            val elementRegion = createRegionFromPath(element.path)

            Log.d("elementRegion", elementRegion.bounds.toString())

            val lassoBounds = lassoRegion.bounds
            if (elementRegion.op(lassoRegion, Region.Op.INTERSECT) || elementRegion.isEmpty) {

                val elementBoundBox = element.path.getBounds()
                val elementBounds = element.path.getBounds().toAndroidRectF().toRect()
                val isInside = lassoBounds.contains(elementBounds)

                if (isInside) {
                    tempSelectedElements.add(element)
                    if (tempRect == Rect.Zero) {
                        tempRect = elementBoundBox
                    } else {
                        tempRect = Rect(
                            left = minOf(tempRect.left, elementBoundBox.left),
                            top = minOf(tempRect.top, elementBoundBox.top),
                            right = maxOf(tempRect.right, elementBoundBox.right),
                            bottom = maxOf(
                                tempRect.bottom,
                                elementBoundBox.bottom
                            )
                        )
                    }
                    continue
                }

                val pathWithOp = Path().apply {
                    this.op(element.path, path, PathOperation.Intersect)
                }


                val isIntersection = pathWithOp.isEmpty.not()

                if (isIntersection) {
                    tempSelectedElements.add(element)
                    if (tempRect == Rect.Zero) {
                        tempRect = elementBoundBox
                    } else {
                        tempRect = Rect(
                            left = minOf(tempRect.left, elementBoundBox.left),
                            top = minOf(tempRect.top, elementBoundBox.top),
                            right = maxOf(tempRect.right, elementBoundBox.right),
                            bottom = maxOf(
                                tempRect.bottom,
                                elementBoundBox.bottom
                            )
                        )
                    }
                }

                Log.d(
                    "isIntersectionisIntersection",
                    "isInside: ${isInside} isIntersection: ${isIntersection}".toString()
                )

            }

        }
        Log.d(
            "isIntersectionisIntersection",
            "time: ${((System.currentTimeMillis() - bef).toString())}"
        )


        Log.d("tempSelectedElements", "${tempSelectedElements.size}")

        if (tempSelectedElements.isNotEmpty()) {
            selectedElements.value = tempSelectedElements
            selectedBoundBox.value = tempRect
            currentMode.value = HandWritingMode.LASSO_MOVE
        } else {
            selectedBoundBox.value = Rect.Zero
            currentMode.value = HandWritingMode.LASSO_SELECTION
        }

        reviseTick.update { it + 1 }
    }

    private fun hitHandWritingPath(
        path: Path,
    ): Pair<HandWritingElement?, Boolean> {

        val eraserRegion = createRegionFromPath(path)
        for (idx in handwritingElements.size - 1 downTo 0) {
            val element = handwritingElements[idx]
            val elementRegion = createRegionFromPath(element.path)

            if (elementRegion.op(eraserRegion, Region.Op.INTERSECT) || elementRegion.isEmpty) {

                val pathWithOp = Path().apply {
                    this.op(element.path, path, PathOperation.Intersect)
                }
                val isIntersection = pathWithOp.isEmpty.not()

                if(isIntersection) {
                    return Pair(element, true)
                }

            }
        }

        return Pair(null, false)
    }

    fun addHandWritingElement(handWritingElement: HandWritingElement) {
        handwritingElements.add(handWritingElement)
        Log.d("handwritingElements", handwritingElements.size.toString())
    }

    fun removeHandWritingElement(handWritingElement: HandWritingElement) {
        if (handwritingElements.remove(handWritingElement)) {
            reviseTick.update { it + 1 }
            Log.d("handwritingElements", handwritingElements.size.toString())
        }
    }

    fun clearHandWritingElements() {
        handwritingElements.clear()
    }


//    /** A [Paint] to draws paths on canvas. */
//    private val drawPaint: MutableState<Paint> = mutableStateOf(defaultPaint())
//
//    /** A radius of the erase circle. */
//    internal var eraseRadius: Float = 50f
//
//    /** A [Paint] to erases paths on canvas, which properties are based on the [drawPaint]. */
//    private val erasePaint: Paint = Paint().apply {
//        shader = null
//        color = Color.White
//        style = PaintingStyle.Fill
//        asFrameworkPaint().xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
//    }
//
//    /** The current [Paint] to draws paths on canvas. */
//    public val currentPaint: Paint
//        get() = if (isEraseMode.value) {
//            erasePaint
//        } else {
//            drawPaint.value
//        }
//
//    private val _currentPaintColor: MutableState<Color> = mutableStateOf(drawPaint.value.color)
//
//    /** [MutableState] of the current color of the [Paint]. */
//    public val currentPaintColor: State<Color> = _currentPaintColor
//
//    /** Background color to be used erasing colored paths. */
//    private var backgroundColor: Color = Color.Transparent
//
//    /** An [ImageBitmap] to draw paths on the canvas. */
//    internal var pathBitmap: ImageBitmap? = null
//
//    /** An [ImageBitmap] to draw a bitmap on the canvas as a background. */
//    internal var imageBitmap: ImageBitmap? = null
//
//    private val _selectedColorIndex: MutableState<Int> = mutableStateOf(2)
//
//    /** State of the selected color index. */
//    internal val selectedColorIndex: State<Int> = _selectedColorIndex
//
//    private val _isEraseMode: MutableState<Boolean> = mutableStateOf(false)
//
//    /** Indicates whether erase mode or not. */
//    public val isEraseMode: State<Boolean> = _isEraseMode
//
//    /** Stack of the drawn [Path]s. */
//    internal val drawPaths = Stack<SketchPath>()
//
//    /** Stack of the redo [Path]s. */
//    private val redoPaths = Stack<SketchPath>()
//
//    private val _canUndo: MutableState<Boolean> = mutableStateOf(false)
//
//    /** Indicates can execute undo or not. */
//    public val canUndo: State<Boolean> = _canUndo
//
//    private val _canRedo: MutableState<Boolean> = mutableStateOf(false)
//
//    /** Indicates can execute redo or not. */
//    public val canRedo: State<Boolean> = _canRedo
//
//    internal val bitmapSize: MutableState<IntSize> = mutableStateOf(IntSize(0, 0))
//
//    internal val imageBitmapMatrix: MutableState<Matrix> = mutableStateOf(Matrix())
//
//    internal var reviseTick = MutableStateFlow(0)
//
//    /** Sets a background color. */
//    public fun setBackgroundColor(color: Color) {
//        backgroundColor = color
//    }
//
//    /** Sets an [ImageBitmap] to draw on the canvas as a background. */
//    public fun setImageBitmap(bitmap: ImageBitmap?) {
//        imageBitmap = bitmap
//    }
//
//    /** Sets an index of the selected color. */
//    public fun setSelectedColorIndex(index: Int) {
//        _selectedColorIndex.value = index
//    }
//
//    /** Sets a new [Paint]. */
//    public fun setPaint(paint: Paint) {
//        drawPaint.value = paint
//        _currentPaintColor.value = paint.color
//    }
//
//    /** Sets an alpha to the [drawPaint]. */
//    public fun setPaintAlpha(alpha: Float) {
//        drawPaint.value.alpha = alpha
//    }
//
//    /** Sets a [Color] to the [drawPaint]. */
//    public fun setPaintColor(color: Color) {
//        drawPaint.value.color = color
//        _currentPaintColor.value = color
//    }
//
//    /** Sets a stroke width to the [drawPaint]. */
//    public fun setPaintStrokeWidth(strokeWidth: Float) {
//        drawPaint.value.strokeWidth = strokeWidth
//    }
//
//    /** Sets a [Shader] to the [drawPaint]. */
//    public fun setPaintShader(shader: Shader?) {
//        drawPaint.value.shader = shader
//    }
//
//    /** Sets a color list as a linear shader to the paint. */
//    public fun setLinearShader(colors: List<Color>) {
//        val size = bitmapSize.value.takeIf { it.width != 0 && it.height != 0 } ?: return
//        setPaintShader(
//            LinearGradientShader(
//                Offset(size.width.toFloat() / 2, 0f),
//                Offset(size.width.toFloat() / 2, size.height.toFloat()),
//                colors,
//            )
//        )
//    }
//
//    /** Sets a rainbow shader to the paint. */
////    public fun setRainbowShader() {
////        setLinearShader(defaultColorList())
////        setSelectedColorIndex(initialSelectedIndex)
////    }
//
//    /** Sets a [PaintingStyle] to the [drawPaint]. */
//    public fun setPaintingStyle(paintingStyle: PaintingStyle) {
//        drawPaint.value.style = paintingStyle
//    }
//
//    /** Sets a [PathEffect] to the [drawPaint]. */
//    public fun setPathEffect(pathEffect: PathEffect?) {
//        drawPaint.value.pathEffect = pathEffect
//    }
//
//    /** Draws a [Path] with the current [Paint]. */
//    public fun addDrawPath(path: Path) {
//        drawPaths.add(SketchPath(path, Paint().copy(currentPaint)))
//    }
//
//    /** Draws a [Path] with the current [Paint]. */
//    public fun addDrawPath(path: Path, paint: Paint) {
//        drawPaths.add(SketchPath(path, Paint().copy(paint)))
//    }
//
//    internal fun clearRedoPath() {
//        redoPaths.clear()
//    }
//
//    internal fun updateRevised() {
//        _canUndo.value = drawPaths.isNotEmpty()
//        _canRedo.value = redoPaths.isNotEmpty()
//    }
//
//    /** Executes undo the drawn path if possible. */
//    public fun undo() {
//        if (canUndo.value) {
//            redoPaths.push(drawPaths.pop())
//            reviseTick.value++
//            updateRevised()
//        }
//    }
//
//    /** Executes redo the drawn path if possible. */
//    public fun redo() {
//        if (canRedo.value) {
//            drawPaths.push(redoPaths.pop())
//            reviseTick.value++
//            updateRevised()
//        }
//    }
//
//    /**
//     * Sets the erase mode or not.
//     *
//     * @param isEraseMode Flag to set erase mode.
//     */
//    public fun setEraseMode(isEraseMode: Boolean) {
//        _isEraseMode.value = isEraseMode
//    }
//
//    /**
//     * Sets the radius size of the erase circle.
//     *
//     * @param eraseRadius Radius of the erase circle.
//     */
//    public fun setEraseRadius(eraseRadius: Float) {
//        this.eraseRadius = eraseRadius
//    }
//
//    /** Toggles the erase mode. */
//    public fun toggleEraseMode() {
//        _isEraseMode.value = !isEraseMode.value
//    }
//
//    /** Clear the drawn paths and redo paths on canvas.. */
//    public fun clearPaths() {
//        drawPaths.clear()
//        redoPaths.clear()
//        updateRevised()
//        reviseTick.value++
//    }
//
//    /** Clear the image bitmap. */
//    public fun clearImageBitmap() {
//        setImageBitmap(null)
//        imageBitmapMatrix.value = Matrix()
//    }
//
//    /** Clear drawn paths and the bitmap image. */
//    public fun clear() {
//        clearPaths()
//        clearImageBitmap()
//    }
//
//    /** Returns the current [Sketchbook]'s bitmap. */
//    public fun getSketchbookBitmap(): ImageBitmap {
//        val size = bitmapSize.value
//        val combinedBitmap = ImageBitmap(size.width, size.height, ImageBitmapConfig.Argb8888)
//        val canvas = Canvas(combinedBitmap)
//        imageBitmap?.let {
//            val immutableBitmap = it.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, false)
//            canvas.nativeCanvas.drawBitmap(immutableBitmap, imageBitmapMatrix.value, null)
//            Log.d("getSketchbookBitmap", "getSketchbookBitmap")
//            immutableBitmap.recycle()
//        }
//        pathBitmap?.let { canvas.drawImage(it, Offset.Zero, Paint()) }
//        return combinedBitmap
//    }
//
//    internal fun releaseBitmap() {
//        pathBitmap?.asAndroidBitmap()?.recycle()
//        pathBitmap = null
//    }
}

internal data class SketchPath(
    val path: Path,
    val paint: Paint
)

/** Returns a default [Paint]. */
internal fun defaultPaint(): Paint {
    return Paint().apply {
        color = Color.White
        strokeWidth = 14f
        isAntiAlias = true
        style = PaintingStyle.Stroke
        strokeJoin = StrokeJoin.Round
        strokeCap = StrokeCap.Round
    }
}

internal fun defaultPaint2(): Paint {
    return Paint().apply {
        color = Color.Red
        strokeWidth = 14f
        isAntiAlias = true
        style = PaintingStyle.Stroke
        strokeJoin = StrokeJoin.Round
        strokeCap = StrokeCap.Round
    }
}

/** Returns a default [Paint]. */
internal fun lassoLinePaint(): Paint {
    return Paint().apply {
        color = Color.Black
        strokeWidth = 14f
        isAntiAlias = true
        style = PaintingStyle.Stroke
        strokeJoin = StrokeJoin.Round
        strokeCap = StrokeCap.Round
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 40f), 10f)
    }
}


internal fun Paint.copy(from: Paint): Paint = apply {
    alpha = from.alpha
    isAntiAlias = from.isAntiAlias
    color = from.color
    blendMode = from.blendMode
    style = from.style
    strokeWidth = from.strokeWidth
    strokeCap = from.strokeCap
    strokeJoin = from.strokeJoin
    strokeMiterLimit = from.strokeMiterLimit
    filterQuality = from.filterQuality
    shader = from.shader
    colorFilter = from.colorFilter
    pathEffect = from.pathEffect
}
//
//fun segmentsIntersect(s1: Segment, s2: Segment): Boolean {
//    val (A, B) = s1
//    val (C, D) = s2
//
//    val ccw1 = ccw(A, B, C) * ccw(A, B, D)
//    val ccw2 = ccw(C, D, A) * ccw(C, D, B)
//
//    if (ccw1 < 0 && ccw2 < 0) return true
//
//    return isCollinearOverlap(A, B, C, D)
//}
//
//data class Point(val x: Double, val y: Double)
//
//data class Segment(val start: Point, val end: Point) {
//    fun intersects(other: Segment): Boolean {
//        return segmentsIntersect(this, other)
//    }
//}
//
//data class Event(val x: Double, val segment: Segment, val type: Int) : Comparable<Event> {
//    override fun compareTo(other: Event): Int = compareValuesBy(this, other, { it.x })
//}
//
//fun ccw(a: Point, b: Point, c: Point): Int {
//    val crossProduct = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)
//    return when {
//        crossProduct > 0 -> 1
//        crossProduct < 0 -> -1
//        else -> 0
//    }
//}
//
//fun isCollinearOverlap(A: Point, B: Point, C: Point, D: Point): Boolean {
//    fun inRange(x: Double, min: Double, max: Double) =
//        x in min.coerceAtMost(max)..max.coerceAtLeast(min)
//
//    return (ccw(A, B, C) == 0 && inRange(C.x, A.x, B.x) && inRange(C.y, A.y, B.y)) ||
//            (ccw(A, B, D) == 0 && inRange(D.x, A.x, B.x) && inRange(D.y, A.y, B.y)) ||
//            (ccw(C, D, A) == 0 && inRange(A.x, C.x, D.x) && inRange(A.y, C.y, D.y)) ||
//            (ccw(C, D, B) == 0 && inRange(B.x, C.x, D.x) && inRange(B.y, C.y, D.y))
//}
//
//
//fun getIntersection(s1: Segment, s2: Segment): Boolean? {
//    return if (segmentsIntersect(s1, s2)) true else null
//}
//
//fun hasIntersectionSweepLine(sn: List<Segment>, sk: List<Segment>): Boolean {
//    val eventQueue = PriorityQueue<Event>()
//    val activeSegments = TreeSet<Segment>(compareBy { it.start.y })
//
//    for (segment in sn) {
//        eventQueue.add(Event(segment.start.x, segment, 1))
//        eventQueue.add(Event(segment.end.x, segment, -1))
//    }
//    for (segment in sk) {
//        eventQueue.add(Event(segment.start.x, segment, 2))
//        eventQueue.add(Event(segment.end.x, segment, -2))
//    }
//
//    while (eventQueue.isNotEmpty()) {
//        val event = eventQueue.poll()
//        val segment = event.segment
//
//        when (event.type) {
//            1 -> activeSegments.add(segment)
//            2 -> {
//                for (other in activeSegments) {
//                    if (getIntersection(segment, other) != null) return true
//                }
//                activeSegments.add(segment)
//            }
//
//            -1, -2 -> activeSegments.remove(segment)
//        }
//    }
//    return false
//}

package com.henni.handwriting.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.util.fastForEachReversed
import com.henni.handwriting.kmp.ext.addPadding
import com.henni.handwriting.kmp.ext.contains
import com.henni.handwriting.kmp.ext.overlaps
import com.henni.handwriting.kmp.ext.translate
import com.henni.handwriting.kmp.ext.unions
import com.henni.handwriting.kmp.ext.updateTick
import com.henni.handwriting.kmp.model.HandwritingPath
import com.henni.handwriting.kmp.model.HitResult
import com.henni.handwriting.kmp.model.Padding
import com.henni.handwriting.kmp.model.ToolMode
import com.henni.handwriting.kmp.model.copy
import com.henni.handwriting.kmp.model.defaultPenPaint
import com.henni.handwriting.kmp.model.defaultStrokeEraserPaint
import com.henni.handwriting.kmp.model.lassoDefaultPaint
import com.henni.handwriting.kmp.operation.InsertOperation
import com.henni.handwriting.kmp.operation.Operation
import com.henni.handwriting.kmp.operation.RemoveOperation
import com.henni.handwriting.kmp.operation.TranslateOperation
import com.henni.handwriting.kmp.tool.LassoMoveTouchEvent
import com.henni.handwriting.kmp.tool.LassoSelectTouchEvent
import com.henni.handwriting.kmp.tool.PenTouchEvent
import com.henni.handwriting.kmp.tool.StrokeEraserTouchEvent
import com.henni.handwriting.kmp.tool.ToolTouchEvent
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun rememberHandwritingController(
    block: HandwritingController.() -> Unit
): HandwritingController {
    return remember {
        HandwritingController().apply(block)
    }
}

class HandwritingController internal constructor(
) {
    val refreshTick = MutableStateFlow<Int>(0)

    var eraserPointRadius by mutableStateOf(20f)

    var isEraserPointShowed by mutableStateOf(true)

    var penPaint by mutableStateOf(defaultPenPaint())

    var eraserPaint by mutableStateOf(defaultStrokeEraserPaint())

    var lassoPaint by mutableStateOf(lassoDefaultPaint())

    var currentPaint by mutableStateOf(penPaint)

    var selectedBoundBoxPaint by mutableStateOf(lassoDefaultPaint())

    var contentBackground by mutableStateOf(Color.Red)

    val handwritingPathCollection = ArrayDeque<HandwritingPath>()

    var currentTouchEvent: ToolTouchEvent by mutableStateOf(PenTouchEvent(this))

    init {

//        repeat(100) {
//            val path = Path()
//            val random1 = Random.nextInt(100, 500).toFloat()
//            val random2 = Random.nextInt(200, 400).toFloat()
//            val random3 = Random.nextInt(200, 600).toFloat()
//            val random4 = Random.nextInt(300, 400).toFloat()
//            val random5 = Random.nextInt(200, 600).toFloat()
//            val random6 = Random.nextInt(300, 400).toFloat()
//
//            val offsets = mutableListOf<Offset>(
//                Offset(random1.toFloat(), random2.toFloat()),
//                Offset(random3.toFloat(), random4.toFloat())
//            )
//
//            path.moveTo(random1, random2)
//            path.quadraticBezierTo(random1, random2, (random1 + random3) / 2, (random3 + random4)/ 2)
//
//            val last = offsets.last()
////            path.lineTo(last.x + 3, last.y + 3)
////            path.lineTo(last.x - 6, last.y + 3)
////            path.lineTo(last.x + 3, last.y - 6)
////            path.lineTo(last.x - 6, last.y - 6)
//
//            offsets.fastForEachReversed {
//                path.lineTo(it.x, it.y)
//            }
//
//            val first = offsets.first()
//            path.lineTo(first.x + 3, first.y + 3)
//            path.lineTo(first.x - 6, first.y + 3)
//            path.lineTo(first.x + 3, first.y - 6)
//            path.lineTo(first.x - 6, first.y - 6)
//
//
//
//            handwritingDataCollection.add(
//                HandwritingData(
//                    path = path,
//                    paint = Paint().copy(penPaint)
//                )
//            )
//        }
    }


    var selectedBoundBox by mutableStateOf(Rect.Zero)

    var selectedBoundBoxPadding by mutableStateOf(Padding.Zero)

    var isSelectedDataHighlight by mutableStateOf(true)

    var selectedDataHighlightColor by mutableStateOf(Color.Red)

    val selectedDataSet = mutableSetOf<HandwritingPath>()

    val isNoteZoomable: MutableState<Boolean> = mutableStateOf(true)

    val toolPointerType: MutableState<PointerType> = mutableStateOf(PointerType.Touch)

    fun setIsNoteZoomable(sZoomable: Boolean) {
        this.isNoteZoomable.value = sZoomable
    }

    fun setIsEraserPointShowed(isShowed: Boolean) {
        this.isEraserPointShowed = isShowed
    }

    fun setIsSelectedDataHighlight(isSelectedDataHighlight: Boolean) {
        this.isSelectedDataHighlight = isSelectedDataHighlight
    }

//    fun setSelectedDataHighlightColor(color: Color) {
//        this.selectedDataHighlightColor = color
//    }

    /** Sets a eraser point radius to the [eraserPointRadius]. */
//    fun setEraserPointRadius(radius: Float) {
//        eraserPointRadius = radius
//    }

    /** Sets a lasso stroke width to the [lassoPaint]. */
    fun setLassoStrokeWidth(width: Float) {
        lassoPaint.strokeWidth = width
    }

    /** Sets a lasso color to the [lassoPaint]. */
    fun setLassoColor(color: Color) {
        lassoPaint.color = color
    }

    /** Sets a lasso stroke width to the [lassoPaint]. */
    fun setSelectedBoxStrokeWidth(width: Float) {
        selectedBoundBoxPaint.strokeWidth = width
    }

    /** Sets a lasso color to the [lassoPaint]. */
    fun setSelectedBoxColor(color: Color) {
        selectedBoundBoxPaint.color = color
    }

    /** Sets a lasso color to the [lassoPaint]. */
    fun setSelectedBoxPadding(padding: Padding) {
        selectedBoundBoxPadding = padding
    }


//    /** Sets a [Paint] to the [penPaint]. */
//    fun setPenPaint(paint: Paint) {
//        penPaint = paint
//    }

    /** Sets a [Color] to the [penPaint]. */
    fun setPenColor(color: Color) {
        penPaint.color = color
    }

    /** Sets a stroke width to the [penPaint]. */
    fun setPenAlpha(alpha: Float) {
        penPaint.alpha = alpha
    }

    /** Sets a stroke width to the [penPaint]. */
    fun setPenStrokeWidth(width: Float) {
        penPaint.strokeWidth = width
    }

    /** Sets a [Shader] to the [penPaint]. */
    fun setPenShader(shader: Shader?) {
        penPaint.shader = shader
    }

    /** Sets a [PaintingStyle] to the [penPaint]. */
    fun setPenPaintingStyle(style: PaintingStyle) {
        penPaint.style = style
    }

    /** Sets a [StrokeJoin] to the [penPaint]. */
    fun setPenStrokeJoin(strokeJoin: StrokeJoin) {
        penPaint.strokeJoin = strokeJoin
    }

    /** Sets a [PathEffect] to the [penPaint]. */
    fun setPenPathEffect(pathEffect: PathEffect?) {
        penPaint.pathEffect = pathEffect
    }


    fun transformSelectedBoundBox(matrix: Matrix) {
        selectedBoundBox = selectedBoundBox.translate(matrix)
    }

    fun setToolMode(toolMode: ToolMode) {

        when (toolMode) {
            ToolMode.PenMode -> {
                currentPaint = penPaint
                currentTouchEvent = PenTouchEvent(this)
            }

            ToolMode.EraserMode -> {
                currentPaint = eraserPaint
                currentTouchEvent = StrokeEraserTouchEvent(this)
            }

            ToolMode.LassoSelectMode -> {
                currentPaint = lassoPaint
                currentTouchEvent = LassoSelectTouchEvent(this)
            }

            ToolMode.LassoMoveMode -> {
                currentPaint = lassoPaint
                currentTouchEvent = LassoMoveTouchEvent(this)
            }
        }


        initializeSelection()
        updateRefreshTick()
    }

    fun addHandWritingPath(
        path: Path,
        deformationPath: Path,
        points: List<Offset>
    ) {
        execute(
            InsertOperation(
                controller = this@HandwritingController,
                data = HandwritingPath(
                    renderedPath = path,
                    hitAreaPath = deformationPath,
                    initialPoints = points,
                    paint = Paint().copy(penPaint),
                )
            )
        )
        updateUndoRedoState()
    }

    fun removeHandWritingPath(path: Path) {
        val hitResult = hitHandWritingPath(path)
        if (hitResult.isHit) {
            hitResult.path?.let {
                execute(
                    RemoveOperation(
                        controller = this@HandwritingController,
                        data = it
                    )
                )
            }
        }
        updateUndoRedoState()
    }

    fun selectHandWritingData(
        path: Path,
    ) {

        val tempSelectedDataSet = mutableSetOf<HandwritingPath>()
        var tempRect = Rect.Zero

        val lassoBounds = path.getBounds()

        handwritingPathCollection.fastForEachReversed { data ->
            println("isConvex: ${data.renderedPath.isConvex}")

            var dataBounds = data.hitAreaPath.getBounds()
            if (dataBounds.isEmpty) {
                dataBounds = Rect(dataBounds.center, 5f)
            }

            if (!lassoBounds.overlaps(dataBounds)) {
                return@fastForEachReversed
            }

            if (lassoBounds.contains(dataBounds)) {
                tempSelectedDataSet.add(data)
                tempRect = tempRect.unions(dataBounds)

                return@fastForEachReversed
            }

            if (overlaps(data.hitAreaPath, path)) {
                tempSelectedDataSet.add(data)
                tempRect = tempRect.unions(dataBounds)

                return@fastForEachReversed
            }

        }

        println("tempSelectedDataSet: ${tempSelectedDataSet.size}")

        if (tempSelectedDataSet.isNotEmpty()) {
            selectedDataSet.clear()
            selectedDataSet.addAll(tempSelectedDataSet)
            selectedBoundBox = tempRect.addPadding(selectedBoundBoxPadding)
            currentTouchEvent = LassoMoveTouchEvent(this)
        } else {
            selectedBoundBox = Rect.Zero
            currentTouchEvent = LassoSelectTouchEvent(this)
        }

        updateRefreshTick()
    }

    private fun hitHandWritingPath(
        hitPath: Path,
    ): HitResult {

        val hitBounds = hitPath.getBounds()

        handwritingPathCollection.fastForEachReversed { data ->

            var dataBounds = data.hitAreaPath.getBounds()
            if (dataBounds.isEmpty) {
                dataBounds = Rect(dataBounds.center, 5f)
            }

            if (hitBounds.overlaps(dataBounds)) {

                val pathWithOp = Path().apply {
                    this.op(data.hitAreaPath, hitPath, PathOperation.Intersect)
                }

                val isIntersect = pathWithOp.isEmpty.not()
                if (isIntersect) {
                    return HitResult(
                        isHit = true,
                        path = data
                    )
                }

            }

        }

        return HitResult(
            isHit = false,
            path = null
        )
    }

    fun addHandWritingData(data: HandwritingPath) {
        handwritingPathCollection.add(data)
        updateRefreshTick()
    }

    fun removeHandWritingData(data: HandwritingPath) {
        if (handwritingPathCollection.remove(data)) {
            updateRefreshTick()
        }
    }

    fun translateHandWritingDataSet(
        translateOffset: Offset
    ) {
        execute(
            TranslateOperation(
                controller = this@HandwritingController,
                dataSet = mutableSetOf<HandwritingPath>().apply {
                    addAll(selectedDataSet)
                },
                offset = translateOffset
            )
        )

        updateUndoRedoState()
    }

    fun clearAllHandWritingData() {
        handwritingPathCollection.clear()
        updateRefreshTick()
    }

    private fun initializeSelection() {
        selectedBoundBox = Rect.Zero
        selectedDataSet.clear()
    }

    fun isDataSelected(data: HandwritingPath): Boolean {
        return selectedDataSet.find {
            it.id == data.id
        } != null
    }


    private fun updateRefreshTick() {
        refreshTick.updateTick()
    }

    /**
     * Operation
     **/

    var canUndo by mutableStateOf(false)

    var canRedo by mutableStateOf(false)

    private val undoOperations: ArrayDeque<Operation> = ArrayDeque()

    private val redoOperations: ArrayDeque<Operation> = ArrayDeque()

    fun isUndoAvailable(): Boolean {
        return undoOperations.isEmpty()
    }

    fun isRedoAvailable(): Boolean {
        return redoOperations.isEmpty()
    }

    private fun execute(operation: Operation) {
        if (operation.doOperation()) {
            undoOperations.add(operation)
            redoOperations.clear()
        }
    }

    fun undo() {
        if (undoOperations.isNotEmpty()) {
            val operation = undoOperations.removeLast()
            operation.undo()
            redoOperations.add(operation)
        }

        currentTouchEvent.onTouchCancel()

        initializeSelection()
        updateRefreshTick()
        updateUndoRedoState()
    }

    fun redo() {
        if (redoOperations.isNotEmpty()) {
            val operation = redoOperations.removeLast()
            operation.redo()
            undoOperations.add(operation)
        }

        currentTouchEvent.onTouchCancel()

        initializeSelection()
        updateRefreshTick()
        updateUndoRedoState()
    }

    private fun updateUndoRedoState() {

        canUndo = !isUndoAvailable()
        canRedo = !isRedoAvailable()

        println("canUndo: ${canUndo}, canRedo: ${canRedo}")
    }


}
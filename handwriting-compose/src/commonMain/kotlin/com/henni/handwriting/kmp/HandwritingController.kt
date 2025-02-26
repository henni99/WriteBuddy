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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.util.fastForEachReversed
import com.henni.handwriting.kmp.model.HandwritingData
import com.henni.handwriting.kmp.model.HitResult
import com.henni.handwriting.kmp.model.Padding
import com.henni.handwriting.kmp.model.ToolMode
import com.henni.handwriting.kmp.model.copy
import com.henni.handwriting.kmp.model.defaultEraserPaint
import com.henni.handwriting.kmp.model.defaultPaint
import com.henni.handwriting.kmp.model.lassoDefaultPaint
import com.henni.handwriting.kmp.operation.InsertOperation
import com.henni.handwriting.kmp.operation.OperationManager
import com.henni.handwriting.kmp.operation.OperationManagerImpl
import com.henni.handwriting.kmp.operation.RemoveOperation
import com.henni.handwriting.kmp.operation.TranslateOperation
import com.henni.handwriting.kmp.tool.LassoMoveTouchEvent
import com.henni.handwriting.kmp.tool.LassoSelectTouchEvent
import com.henni.handwriting.kmp.tool.PenTouchEvent
import com.henni.handwriting.kmp.tool.StrokeEraserTouchEvent
import com.henni.handwriting.kmp.tool.ToolTouchEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

@Composable
fun rememberHandwritingController(
    block: HandwritingController.() -> Unit
): HandwritingController {
    return remember {
        HandwritingController().apply(block)
    }
}

class HandwritingController internal constructor(
    private val operationManager: OperationManager = OperationManagerImpl()
) {
    val refreshTick = MutableStateFlow<Int>(0)

    var currentToolMode by mutableStateOf(ToolMode.PenMode)

    var eraserPointRadius by mutableStateOf(20f)

    var isEraserPointShowed by mutableStateOf(true)

    var penPaint by mutableStateOf(defaultPaint())

    var eraserPaint by mutableStateOf(defaultEraserPaint())

    var lassoPaint by mutableStateOf(lassoDefaultPaint())

    var selectedBoundBoxPaint by mutableStateOf(lassoDefaultPaint())

    var contentBackground by mutableStateOf(Color.Red)

    val handwritingDataCollection = ArrayDeque<HandwritingData>()


    var curTouchEvent: ToolTouchEvent by mutableStateOf(PenTouchEvent(this))

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

    val selectedDataSet = mutableSetOf<HandwritingData>()

    val isNoteZoomable: MutableState<Boolean> = mutableStateOf(true)

    val toolPointerType: MutableState<PointerType> = mutableStateOf(PointerType.Touch)


    /** Sets [Paint] to the [currentPaint]. */
    fun updateCurrentPaint() {

    }

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
        selectedBoundBox = selectedBoundBox.translate(
            matrix.values[12],
            matrix.values[13]
        )
    }

    fun setToolMode(toolMode: ToolMode) {
        currentToolMode = toolMode

        when (toolMode) {
            ToolMode.PenMode -> {
                curTouchEvent = PenTouchEvent(this)
            }

            ToolMode.EraserMode -> {

                curTouchEvent = StrokeEraserTouchEvent(this)
            }

            ToolMode.LassoSelectMode -> {
                curTouchEvent = LassoSelectTouchEvent(this)
            }

            ToolMode.LassoMoveMode -> {
                curTouchEvent = LassoMoveTouchEvent(this)
            }

            else -> {}
        }


        initializeSelection()
        updateRefreshTick()
    }

    fun addHandWritingPath(
        path: Path,
        deformationPath: Path,
        points: List<Offset>
    ) {
        operationManager.executeOperation(
            InsertOperation(
                controller = this@HandwritingController,
                data = HandwritingData(
                    path = path,
                    deformationPath = deformationPath,
                    originalOffsets = points,
                    paint = Paint().copy(penPaint),
                )
            )
        )
        updateOperationState()
    }

    fun removeHandWritingPath(path: Path) {
        val hitResult = hitHandWritingPath(path)
        if (hitResult.isHit) {
            hitResult.data?.let {
                operationManager.executeOperation(
                    RemoveOperation(
                        controller = this@HandwritingController,
                        data = it
                    )
                )
            }
        }
        updateOperationState()
    }

    fun selectHandWritingData(
        path: Path,
    ) {

        val tempSelectedDataSet = mutableSetOf<HandwritingData>()
        var tempRect = Rect.Zero

        val lassoBounds = path.getBounds()

        handwritingDataCollection.fastForEachReversed { data ->
            println("isConvex: ${data.path.isConvex}")

            var dataBounds = data.deformationPath.getBounds()
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

            if (overlaps(data.deformationPath, path)) {
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
            currentToolMode = ToolMode.LassoMoveMode
            curTouchEvent = LassoMoveTouchEvent(this)
        } else {
            selectedBoundBox = Rect.Zero
            currentToolMode = ToolMode.LassoSelectMode
            curTouchEvent = LassoSelectTouchEvent(this)
        }

        updateRefreshTick()
    }

    private fun hitHandWritingPath(
        hitPath: Path,
    ): HitResult {

        val hitBounds = hitPath.getBounds()

        handwritingDataCollection.fastForEachReversed { data ->

            var dataBounds = data.deformationPath.getBounds()
            if (dataBounds.isEmpty) {
                dataBounds = Rect(dataBounds.center, 5f)
            }

            if (hitBounds.overlaps(dataBounds)) {

                val pathWithOp = Path().apply {
                    this.op(data.deformationPath, hitPath, PathOperation.Intersect)
                }

                val isIntersect = pathWithOp.isEmpty.not()
                if (isIntersect) {
                    return HitResult(
                        isHit = true,
                        data = data
                    )
                }

            }

        }

        return HitResult(
            isHit = false,
            data = null
        )
    }

    fun addHandWritingData(data: HandwritingData) {
        handwritingDataCollection.add(data)
    }

    fun removeHandWritingData(data: HandwritingData) {
        if (handwritingDataCollection.remove(data)) {
            refreshTick.updateTick()

        }
    }

    fun translateHandWritingDataSet(
        translateOffset: Offset
    ) {
        operationManager.executeOperation(
            TranslateOperation(
                controller = this@HandwritingController,
                dataSet = mutableSetOf<HandwritingData>().apply {
                    addAll(selectedDataSet)
                },
                offset = translateOffset
            )
        )

        updateOperationState()
    }

    fun clearAllHandWritingData() {
        handwritingDataCollection.clear()
        updateRefreshTick()
    }


    /** undo, redo **/
    val canUndo: MutableState<Boolean> = mutableStateOf(false)
    val canRedo: MutableState<Boolean> = mutableStateOf(false)


    fun updateOperationState() {
        println("updateOperationState: ${operationManager.undoStack}, ${operationManager.redoStack.size}")

        canUndo.value = operationManager.isUndoNotEmpty()
        canRedo.value = operationManager.isRedoNotEmpty()

        println("canUndo: ${canUndo}, canRedo: ${canRedo}")
    }

    fun undo() {
        operationManager.undo()
        updateRefreshTick()
        updateOperationState()
    }

    fun redo() {
        operationManager.redo()
        updateRefreshTick()
        updateOperationState()
    }

    fun initializeSelection() {
        selectedBoundBox = Rect.Zero
        selectedDataSet.clear()
    }

    fun isDataSelected(data: HandwritingData): Boolean {
        return selectedDataSet.find {
            it.id == data.id
        } != null
    }


    private fun updateRefreshTick() {
        refreshTick.updateTick()
    }
}
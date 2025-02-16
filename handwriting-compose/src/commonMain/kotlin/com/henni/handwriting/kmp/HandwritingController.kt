package com.henni.handwriting.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.util.fastForEachReversed
import com.henni.handwriting.kmp.model.HandwritingData
import com.henni.handwriting.kmp.model.HitResult
import com.henni.handwriting.kmp.model.Padding
import com.henni.handwriting.kmp.model.ToolMode
import com.henni.handwriting.kmp.model.copy
import com.henni.handwriting.kmp.model.defaultPaint
import com.henni.handwriting.kmp.model.lassoDefaultPaint
import com.henni.handwriting.kmp.operation.InsertOperation
import com.henni.handwriting.kmp.operation.OperationManager
import com.henni.handwriting.kmp.operation.OperationManagerImpl
import com.henni.handwriting.kmp.operation.RemoveOperation
import com.henni.handwriting.kmp.operation.TranslateOperation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

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

    val currentToolMode: MutableState<ToolMode> = mutableStateOf(ToolMode.PenMode)

    val eraserPointRadius: MutableState<Float> = mutableStateOf(20f)

    val isEraserPointShowed: MutableState<Boolean> = mutableStateOf(true)

    val penPaint: MutableState<Paint> = mutableStateOf(defaultPaint())

    val eraserPaint: MutableState<Paint> = mutableStateOf(defaultPaint())

    val lassoPaint: MutableState<Paint> = mutableStateOf(lassoDefaultPaint())

    val selectedBoundBoxPaint: MutableState<Paint> = mutableStateOf(lassoDefaultPaint())


    val handwritingDataCollection = ArrayDeque<HandwritingData>()

    val selectedBoundBox: MutableState<Rect> = mutableStateOf(Rect.Zero)

    val selectedBoundBoxPadding: MutableState<Padding> = mutableStateOf(Padding.Zero)

    val selectedDataSet = mutableSetOf<HandwritingData>()

    val isNoteZoomable: MutableState<Boolean> = mutableStateOf(true)

    /** Sets [Paint] to the [currentPaint]. */
    fun updateCurrentPaint() {

    }

    fun setIsNoteZoomable(sZoomable: Boolean) {
        this.isNoteZoomable.value = sZoomable
    }

    fun setIsEraserPointShowed(isShowed: Boolean) {
        isEraserPointShowed.value = isShowed
    }

    /** Sets a eraser point radius to the [eraserPointRadius]. */
    fun setEraserPointRadius(radius: Float) {
        eraserPointRadius.value = radius
    }

    /** Sets a lasso stroke width to the [lassoPaint]. */
    fun setLassoStrokeWidth(width: Float) {
        lassoPaint.value.strokeWidth = width
    }

    /** Sets a lasso color to the [lassoPaint]. */
    fun setLassoColor(color: Color) {
        lassoPaint.value.color = color
    }

    /** Sets a lasso stroke width to the [lassoPaint]. */
    fun setSelectedBoxStrokeWidth(width: Float) {
        selectedBoundBoxPaint.value.strokeWidth = width
    }

    /** Sets a lasso color to the [lassoPaint]. */
    fun setSelectedBoxColor(color: Color) {
        selectedBoundBoxPaint.value.color = color
    }

    /** Sets a lasso color to the [lassoPaint]. */
    fun setSelectedBoxPadding(padding: Padding) {
        selectedBoundBoxPadding.value = padding
    }



    /** Sets a [Paint] to the [penPaint]. */
    fun setPenPaint(paint: Paint) {
        penPaint.value = paint
    }

    /** Sets a [Color] to the [penPaint]. */
    fun setPenColor(color: Color) {
        penPaint.value.color = color
    }

    /** Sets a stroke width to the [penPaint]. */
    fun setPenAlpha(alpha: Float) {
        penPaint.value.alpha = alpha
    }

    /** Sets a stroke width to the [penPaint]. */
    fun setPenStrokeWidth(width: Float) {
        penPaint.value.strokeWidth = width
    }

    /** Sets a [Shader] to the [penPaint]. */
    fun setPenShader(shader: Shader?) {
        penPaint.value.shader = shader
    }

    /** Sets a [PaintingStyle] to the [penPaint]. */
    fun setPenPaintingStyle(style: PaintingStyle) {
        penPaint.value.style = style
    }

    /** Sets a [StrokeJoin] to the [penPaint]. */
    fun setPenStrokeJoin(strokeJoin: StrokeJoin) {
        penPaint.value.strokeJoin = strokeJoin
    }

    /** Sets a [PathEffect] to the [penPaint]. */
    fun setPenPathEffect(pathEffect: PathEffect?) {
        penPaint.value.pathEffect = pathEffect
    }


    fun transformSelectedBoundBox(matrix: Matrix) {
        selectedBoundBox.value = selectedBoundBox.value.translate(
            matrix.values[12],
            matrix.values[13]
        )
    }

    fun setToolMode(toolMode: ToolMode) {
        currentToolMode.value = toolMode
        initializeSelection()
        updateRefreshTick()
    }

    fun addHandWritingPath(
        path: Path,
        points: MutableList<Offset>
    ) {
        operationManager.executeOperation(
            InsertOperation(
                controller = this@HandwritingController,
                data = HandwritingData(
                    path = path,
                    originalOffsets = points,
                    paint = Paint().copy(penPaint.value),
                )
            )
        )
    }

    fun removeHandWritingPath(path: Path) {
        val hitResult = hitHandWritingPath(path)
        println("hitResult: ${hitResult}")
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
    }

    fun selectHandWritingData(
        path: Path,
    ) {

        val tempSelectedDataSet = mutableSetOf<HandwritingData>()
        var tempRect = Rect.Zero

        val lassoBounds = path.getBounds()

        handwritingDataCollection.fastForEachReversed { data ->
            val dataBounds = data.path.getBounds()

            if (lassoBounds.overlaps(dataBounds) || dataBounds.isEmpty) {

                if (lassoBounds.contains(dataBounds) || dataBounds.contains(lassoBounds)) {
                    tempSelectedDataSet.add(data)
                    tempRect = tempRect.unions(dataBounds)
                }

                if (overlaps(data.path, path)) {
                    tempSelectedDataSet.add(data)
                    tempRect = tempRect.unions(dataBounds)
                }
            }
        }


        if (tempSelectedDataSet.isNotEmpty()) {
            selectedDataSet.clear()
            selectedDataSet.addAll(tempSelectedDataSet)
            selectedBoundBox.value = tempRect.addPadding(selectedBoundBoxPadding.value)
            currentToolMode.value = ToolMode.LassoMoveMode
        } else {
            selectedBoundBox.value = Rect.Zero
            currentToolMode.value = ToolMode.LassoSelectMode
        }

        updateRefreshTick()
    }

    private fun hitHandWritingPath(
        hitPath: Path,
    ): HitResult {

        val hitBounds = hitPath.getBounds()

        handwritingDataCollection.fastForEachReversed { data ->

            val dataBounds = data.path.getBounds()

            if (hitBounds.overlaps(dataBounds) || dataBounds.isEmpty) {
                val pathWithOp = Path().apply {
                    this.op(data.path, hitPath, PathOperation.Intersect)
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
    }

    fun clearAllHandWritingData() {
        handwritingDataCollection.clear()
        updateRefreshTick()
    }


    /** undo, redo **/
    val canUndo = MutableStateFlow<Boolean>(false)

    val canRedo = MutableStateFlow<Boolean>(false)


    fun updateOperationState() {
        canUndo.update { operationManager.isUndoNotEmpty() }
        canRedo.update { operationManager.isRedoNotEmpty() }
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
        selectedBoundBox.value = Rect.Zero
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
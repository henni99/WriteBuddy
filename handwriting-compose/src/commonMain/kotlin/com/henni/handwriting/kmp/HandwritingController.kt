package com.henni.handwriting.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.util.fastForEachReversed
import com.henni.handwriting.kmp.model.HandwritingData
import com.henni.handwriting.kmp.model.HitResult
import com.henni.handwriting.kmp.model.ToolMode
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
public fun rememberHandwritingController(): HandwritingController {
    return remember { HandwritingController() }
}

@Stable
class HandwritingController internal constructor(
    private val operationManager: OperationManager = OperationManagerImpl()
) {
    val refreshTick = MutableStateFlow<Int>(0)

    val activeToolMode: MutableState<ToolMode> = mutableStateOf(ToolMode.PenMode)

    var activePaint: MutableState<Paint> = mutableStateOf(defaultPaint())

    val handwritingDataCollection = ArrayDeque<HandwritingData>()

    val selectedBoundBox: MutableState<Rect> = mutableStateOf(Rect.Zero)

    val selectedDataSet = mutableSetOf<HandwritingData>()

    fun isDataSelected(data: HandwritingData): Boolean {
        return selectedDataSet.find {
            it.id == data.id
        } != null
    }

    fun transformSelectedBoundBox(matrix: Matrix) {
        selectedBoundBox.value = selectedBoundBox.value.translate(
            matrix.values[12],
            matrix.values[13]
        )
    }

    fun updateToolMode(toolMode: ToolMode) {
        initializeSelection()
        activeToolMode.value = toolMode

        when (toolMode) {
            ToolMode.None, ToolMode.PenMode -> {
                activePaint.value = defaultPaint()
            }

            ToolMode.EraserMode -> {
                activePaint.value = defaultPaint()
            }

            ToolMode.LassoSelectMode -> {
                activePaint.value = lassoDefaultPaint()
            }

            else -> {}
        }

        refreshTick.updateTick()
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
                    paint = activePaint.value,
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
        path: Path
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
            selectedBoundBox.value = tempRect
            activeToolMode.value = ToolMode.LassoMoveMode
        } else {
            selectedBoundBox.value = Rect.Zero
            activeToolMode.value = ToolMode.LassoSelectMode
        }

        refreshTick.updateTick()
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

    private fun updateRefreshTick() {
        refreshTick.updateTick()
    }
}
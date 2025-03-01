package com.henni.handwriting.kmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
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
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.util.fastForEachReversed
import com.henni.handwriting.kmp.extension.addPadding
import com.henni.handwriting.kmp.extension.contains
import com.henni.handwriting.kmp.extension.overlaps
import com.henni.handwriting.kmp.extension.translate
import com.henni.handwriting.kmp.extension.unions
import com.henni.handwriting.kmp.extension.updateTick
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

/**
 * Creates and remembers an instance of [HandwritingController].
 * This ensures that the controller instance persists across recompositions.
 *
 * @param block A lambda function that allows customization of the controller upon initialization.
 * @return A remembered instance of [HandwritingController].
 */
@Composable
fun rememberHandwritingController(
    block: HandwritingController.() -> Unit
): HandwritingController {
    return remember {
        HandwritingController().apply(block)
    }
}

/**
 * Controller for managing handwriting data, such as pen strokes, eraser actions,
 * and lasso selection. It supports undo/redo functionality, tool modes, and manages
 * the state for various elements like strokes, selected paths, and the lasso area.
 */

@Stable
class HandwritingController {

    /**
     * Mutable state to track refresh ticks for UI updates
     */
    val refreshTick = MutableStateFlow<Int>(0)

    // ==============================
    // Pen-related properties
    // ==============================

    /**
     * The paint settings for the pen tool.
     */
    var penPaint by mutableStateOf(defaultPenPaint())

    // ==============================
    // Eraser-related properties
    // ==============================

    /**
     * The paint settings for the eraser tool.
     */
    var eraserPaint by mutableStateOf(defaultStrokeEraserPaint())

    /**
     * The radius of the eraser point.
     */
    var eraserPointRadius by mutableStateOf(20f)

    /**
     * Determines whether the eraser point is visible.
     */
    var isEraserPointShowed by mutableStateOf(true)

    // ==============================
    // Lasso selection properties
    // ==============================

    /**
     * The paint settings for the lasso selection tool.
     */
    var lassoPaint by mutableStateOf(lassoDefaultPaint())

    /**
     * The bounding box representing the selected area during lasso selection.
     */
    var lassoBoundBox by mutableStateOf(Rect.Zero)

    /**
     * The paint settings for the lasso bounding box.
     */
    var lassoBoundBoxPaint by mutableStateOf(lassoDefaultPaint())

    /**
     * The padding applied to the lasso bounding box.
     */
    var lassoBoundBoxPadding by mutableStateOf(Padding.Zero)

    // ==============================
    // Current tool-related properties
    // ==============================

    /**
     * The currently active paint settings (pen or eraser).
     */
    var currentPaint by mutableStateOf(penPaint)

    /**
     * The current touch event handler based on the selected tool.
     */
    var currentTouchEvent: ToolTouchEvent by mutableStateOf(PenTouchEvent(this))

    /**
     * The background color of the handwriting canvas.
     */
    var contentBackground by mutableStateOf(Color.Red)

    /**
     * Determines whether zooming is enabled for the canvas.
     */
    var isZoomable by mutableStateOf(true)

    // ==============================
    // Path storage collections
    // ==============================

    /**
     * A collection of all handwriting paths on the canvas.
     */
    val handwritingPaths = ArrayDeque<HandwritingPath>()

    /**
     * A set of currently selected handwriting paths.
     */
    val selectedHandwritingPaths = mutableSetOf<HandwritingPath>()


    /** Sets a lasso stroke width to the [lassoPaint]. */
    fun setLassoStrokeWidth(width: Float) {
        lassoPaint.strokeWidth = width
    }

    /** Sets a lasso color to the [lassoPaint]. */
    fun setLassoColor(color: Color) {
        lassoPaint.color = color
    }

    /** Sets a lassoBoundBox stroke width to the [lassoPaint]. */
    fun setLassoBoundBoxStrokeWidth(width: Float) {
        lassoBoundBoxPaint.strokeWidth = width
    }

    /** Sets a lassoBoundBox [Color] to the [lassoPaint]. */
    fun setLassoBoundBoxColor(color: Color) {
        lassoBoundBoxPaint.color = color
    }

    /** Sets a [Color] to the [penPaint]. */
    fun setPenColor(color: Color) {
        penPaint.color = color
    }

    /** Sets a pen alpha to the [penPaint]. */
    fun setPenAlpha(alpha: Float) {
        penPaint.alpha = alpha
    }

    /** Sets a pen stroke width to the [penPaint]. */
    fun setPenStrokeWidth(width: Float) {
        penPaint.strokeWidth = width
    }

    /** Sets a pen [Shader] to the [penPaint]. */
    fun setPenShader(shader: Shader?) {
        penPaint.shader = shader
    }

    /** Sets a pen [PaintingStyle] to the [penPaint]. */
    fun setPenStyle(style: PaintingStyle) {
        penPaint.style = style
    }

    /** Sets a pen [StrokeJoin] to the [penPaint]. */
    fun setPenStrokeJoin(strokeJoin: StrokeJoin) {
        penPaint.strokeJoin = strokeJoin
    }

    /** Sets a pen [PathEffect] to the [penPaint]. */
    fun setPenPathEffect(pathEffect: PathEffect?) {
        penPaint.pathEffect = pathEffect
    }

    /** Sets [Paint], [PenTouchEvent] to the [toolMode]. */
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
    }

    /**
     * Adds a new handwriting path to the list of recorded paths.
     * This method updates the list of handwriting paths and triggers a refresh tick
     * to notify the UI about the change.
     *
     * @param path The handwriting path to be added to the list.
     */
    fun addHandWritingPath(path: HandwritingPath) {
        handwritingPaths.add(path)
        updateRefreshTick()
    }

    /**
     * Adds a list of handwriting paths to the existing list.
     * This method appends all the provided paths to the `handwritingPaths` collection.
     * It also updates the refresh tick after the paths are added.
     *
     * @param paths A list of [HandwritingPath] objects to be added to the collection.
     */
    fun addHandWritingPaths(paths: List<HandwritingPath>) {
        handwritingPaths.addAll(paths)
        updateRefreshTick()
    }

    /**
     * Adds a new handwriting path to the list with the provided rendered path, hit area, and points.
     * This method creates a new `HandwritingPath` object and performs an insert operation to add it to the list.
     * It also updates the undo/redo state after the operation.
     *
     * @param renderedPath The path representing the drawn handwriting.
     * @param hitAreaPath The path used for collision detection or interaction with the rendered path.
     * @param offsets The list of initial points that represent the path's drawing trajectory.
     */
    fun addHandWritingPath(
        renderedPath: Path,
        hitAreaPath: Path,
        offsets: List<Offset>
    ) {
        execute(
            InsertOperation(
                controller = this@HandwritingController,
                path = HandwritingPath(
                    renderedPath = renderedPath,
                    hitAreaPath = hitAreaPath,
                    initialPoints = offsets,
                    paint = Paint().copy(penPaint),
                )
            )
        )
        updateUndoRedoState()
    }

    /**
     * Removes the specified handwriting path from the collection.
     * If the path is found and successfully removed from the `handwritingPaths` collection,
     * the refresh tick is updated to trigger necessary UI updates.
     *
     * @param path The [HandwritingPath] to be removed from the collection.
     */
    fun removeHandWritingPath(path: HandwritingPath) {
        if (handwritingPaths.remove(path)) {
            updateRefreshTick()
        }
    }

    /**
     * Removes a handwriting path by matching it with a provided `Path` object.
     * If the provided `Path` overlaps any existing handwriting path, the matched handwriting path
     * is removed through an `Operation` (remove).
     * The undo/redo state is updated after removal.
     *
     * @param path The [Path] to check for overlap and remove the corresponding `HandwritingPath`.
     */
    fun removeHandWritingPath(path: Path) {
        val hitResult = hitHandWritingPath(path)
        if (hitResult.isHit) {
            hitResult.path?.let {
                execute(
                    RemoveOperation(
                        controller = this@HandwritingController,
                        path = it
                    )
                )
            }
        }
        updateUndoRedoState()
    }

    /**
     *
     * Checks if the provided `hitPath` overlaps with any existing handwriting paths.
     * The function iterates through the `handwritingPaths` collection in reverse order,
     * and for each path, it checks whether the bounding box of the `hitPath` overlaps with the
     * bounding box of the handwriting path. If an overlap is detected, it further checks if the
     * paths truly overlap and returns the matching `HandwritingPath` for Eraser.
     *
     * If no overlap is found, it returns a `HitResult` indicating no match.
     *
     * @param hitPath The `Path` to check for overlap with existing handwriting paths.
     * @return A `HitResult` object that contains whether a hit was found and the corresponding path if a hit is detected.
     */
    private fun hitHandWritingPath(
        hitPath: Path,
    ): HitResult {

        val hitBounds = hitPath.getBounds()

        handwritingPaths.fastForEachReversed { data ->

            var dataBounds = data.hitAreaPath.getBounds()
            if (dataBounds.isEmpty) {
                dataBounds = Rect(dataBounds.center, 5f)
            }

            if (!hitBounds.overlaps(dataBounds)) {
                return@fastForEachReversed
            }

            if (overlaps(data.hitAreaPath, hitPath)) {
                return HitResult(
                    isHit = true,
                    path = data
                )
            }
        }

        return HitResult(
            isHit = false,
            path = null
        )
    }

    /**
     * Selects handwriting paths that are within or overlapping the given `path`, typically used
     * for lasso selection. The function first calculates the bounding box of the provided `path`
     * and checks each handwriting path in the `handwritingPaths` collection to see if its hit area
     * overlaps with or is completely contained within the bounding box of the lasso selection.
     *
     * If a handwriting path is selected, it adds it to a temporary set of selected paths and updates
     * the bounding box to encompass all selected paths. The function then transitions the current touch event
     * to a `LassoMoveTouchEvent` to allow further manipulation of the selected paths.
     *
     * If no paths are selected, the lasso bounding box is reset and the current touch event is set to
     * `LassoSelectTouchEvent` to enable a new selection.
     *
     * @param path The `Path` representing the lasso selection, typically drawn by the user.
     */
    fun selectHandWritingPath(
        path: Path,
    ) {

        val tempSelectedPaths = mutableSetOf<HandwritingPath>()
        var tempBounds = Rect.Zero

        val lassoBounds = path.getBounds()

        handwritingPaths.fastForEachReversed { data ->
            var dataBounds = data.hitAreaPath.getBounds()
            if (dataBounds.isEmpty) {
                dataBounds = Rect(dataBounds.center, 5f)
            }

            if (!lassoBounds.overlaps(dataBounds)) {
                return@fastForEachReversed
            }

            if (lassoBounds.contains(dataBounds)) {
                tempSelectedPaths.add(data)
                tempBounds = tempBounds.unions(dataBounds)

                return@fastForEachReversed
            }

            if (overlaps(data.hitAreaPath, path)) {
                tempSelectedPaths.add(data)
                tempBounds = tempBounds.unions(dataBounds)

                return@fastForEachReversed
            }
        }

        if (tempSelectedPaths.isNotEmpty()) {
            selectedHandwritingPaths.clear()
            selectedHandwritingPaths.addAll(tempSelectedPaths)
            lassoBoundBox = tempBounds.addPadding(lassoBoundBoxPadding)
            currentTouchEvent = LassoMoveTouchEvent(this)
        } else {
            lassoBoundBox = Rect.Zero
            currentTouchEvent = LassoSelectTouchEvent(this)
        }

        updateRefreshTick()
    }

    /**
     * Translates (moves) the selected handwriting paths by the specified offset.
     * This operation applies a translation to the selected paths and updates the internal state
     * to reflect the changes. The paths are moved by the given `translateOffset`.
     *
     * The function uses the [TranslateOperation] to perform the translation and ensure that the
     * operation is recorded for undo/redo functionality. Once the translation is applied, it also
     * updates the undo/redo state.
     *
     * @param translateOffset The offset by which to move the selected handwriting paths.
     */
    fun translateHandWritingPaths(
        translateOffset: Offset
    ) {
        execute(
            TranslateOperation(
                controller = this@HandwritingController,
                paths = selectedHandwritingPaths.toMutableSet(),
                offset = translateOffset
            )
        )

        updateUndoRedoState()
    }

    /**
     * Translates the lasso bounding box by applying the given transformation matrix.
     * The function modifies the current `lassoBoundBox` by translating it using the provided
     * `matrix`. This is typically used for moving the lasso selection on the canvas.
     *
     * @param matrix The matrix containing the transformation values to apply to the lasso bounding box.
     */
    fun translateLassoBoundBox(matrix: Matrix) {
        lassoBoundBox = lassoBoundBox.translate(matrix)
    }

    /**
     * Clears all handwriting paths from the canvas.
     * This function removes all paths from the `handwritingPaths` list and resets the state.
     * It is typically used when the user wants to start with a fresh canvas or clear the drawing.
     */
    fun clearAllHandWritingPaths() {
        handwritingPaths.clear()
        updateRefreshTick()
    }

    /**
     * Initializes the selection state by clearing the lasso bounding box and selected handwriting paths.
     * This function resets the selection, ensuring that no paths are currently selected, and the lasso
     * bounding box is cleared.
     * It is typically used when starting a new selection or when resetting the current selection state.
     */
    private fun initializeSelection() {
        lassoBoundBox = Rect.Zero
        selectedHandwritingPaths.clear()
        updateRefreshTick()
    }

    /**
     * Updates the refresh tick by triggering the `updateTick` method on the `refreshTick` state.
     * This function is used to signal that a change has occurred and the UI may need to be refreshed.
     * Typically used after modifying any data that impacts the display of handwriting paths or selection state.
     */
    private fun updateRefreshTick() {
        refreshTick.updateTick()
    }

    /**
     * Operation
     **/

    /**
     * Flag to indicate whether an undo operation can be performed.
     */
    var canUndo by mutableStateOf(false)

    /**
     * Flag to indicate whether a redo operation can be performed.
     */
    var canRedo by mutableStateOf(false)

    /**
     * A deque (double-ended queue) holding the operations for undoing.
     * Stores the sequence of operations that can be reverted.
     */
    private val undoOperations: ArrayDeque<Operation> = ArrayDeque()

    /**
     * A deque (double-ended queue) holding the operations for redoing.
     * Stores the sequence of operations that can be reapplied after an undo.
     */
    private val redoOperations: ArrayDeque<Operation> = ArrayDeque()


    /**
     * Executes the given operation.
     *
     * If the operation is successful, it is added to the undo stack, and the redo stack is cleared.
     * This is done to maintain the correct state for undo/redo functionality.
     *
     * @param operation The operation to be executed.
     */
    private fun execute(operation: Operation) {
        if (operation.doOperation()) {
            undoOperations.add(operation)
            redoOperations.clear()
        }
    }

    /**
     * Performs the undo operation.
     *
     * If there are operations in the undo stack, the most recent operation is undone,
     * and then added to the redo stack. Afterward, the current touch event is reinitialized
     * and the selection is reset.
     */
    fun undo() {
        if (undoOperations.isNotEmpty()) {
            val operation = undoOperations.removeLast()
            operation.undo()
            redoOperations.add(operation)
        }

        currentTouchEvent.onTouchInitialize()

        initializeSelection()
        updateUndoRedoState()
    }

    /**
     * Performs the redo operation.
     *
     * If there are operations in the redo stack, the most recent redo operation is reapplied,
     * and then added back to the undo stack. Afterward, the current touch event is reinitialized
     * and the selection is reset.
     */
    fun redo() {
        if (redoOperations.isNotEmpty()) {
            val operation = redoOperations.removeLast()
            operation.redo()
            undoOperations.add(operation)
        }

        currentTouchEvent.onTouchInitialize()

        initializeSelection()
        updateUndoRedoState()
    }

    /**
     * Updates the state of the undo and redo flags (`canUndo` and `canRedo`).
     * Sets `canUndo` to true if there are operations in the undo stack,
     * and `canRedo` to true if there are operations in the redo stack.
     */
    private fun updateUndoRedoState() {
        canUndo = !undoOperations.isEmpty()
        canRedo = !redoOperations.isEmpty()
    }

}
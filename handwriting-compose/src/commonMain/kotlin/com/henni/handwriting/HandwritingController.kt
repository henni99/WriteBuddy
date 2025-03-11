package com.henni.handwriting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.util.fastForEachReversed
import com.henni.handwriting.extension.addPadding
import com.henni.handwriting.extension.contains
import com.henni.handwriting.extension.overlaps
import com.henni.handwriting.extension.translate
import com.henni.handwriting.extension.unions
import com.henni.handwriting.extension.updateTick
import com.henni.handwriting.model.HandwritingPath
import com.henni.handwriting.model.HitResult
import com.henni.handwriting.model.Padding
import com.henni.handwriting.model.ToolMode
import com.henni.handwriting.model.copy
import com.henni.handwriting.model.defaultLaserPaint
import com.henni.handwriting.model.defaultLassoPaint
import com.henni.handwriting.model.defaultPenPaint
import com.henni.handwriting.model.defaultStrokeEraserPaint
import com.henni.handwriting.operation.InsertOperation
import com.henni.handwriting.operation.Operation
import com.henni.handwriting.operation.RemoveOperation
import com.henni.handwriting.operation.TranslateOperation
import com.henni.handwriting.tool.LassoMoveTouchEvent
import com.henni.handwriting.tool.LassoSelectTouchEvent
import com.henni.handwriting.tool.LineLaserPointerTouchEvent
import com.henni.handwriting.tool.PenTouchEvent
import com.henni.handwriting.tool.StrokeEraserTouchEvent
import com.henni.handwriting.tool.ToolTouchEvent
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Creates and remembers an instance of [HandwritingController].
 * This ensures that the controller instance persists across recompositions and maintains its state.
 *
 * @param isZoomable Whether the handwriting canvas is zoomable. Default is true.
 * @param isEraserPointShowed Whether the eraser point is visible. Default is true.
 * @param penColor The color of the pen used for writing. Default is black.
 * @param penStrokeWidth The stroke width of the pen. Default is 14f.
 * @param eraserPointColor The color of the eraser point. Default is light gray.
 * @param eraserPointRadius The radius of the eraser point. Default is 20f.
 * @param lassoColor The color of the lasso tool. Default is black.
 * @param lassoBoundBoxColor The color of the bounding box of the lasso tool. Default is black.
 * @param lassoBoundBoxPadding The padding around the lasso bounding box. Default is (20, 20, 20, 20).
 * @param laserColor The color of the laser pointer. Default is black.
 * @param contentBackground The background color of the content area. Default is white.
 *
 * @return A remembered instance of [HandwritingController] with the specified configuration.
 */

@Composable
fun rememberHandwritingController(
  isZoomable: Boolean = true,
  isEraserPointShowed: Boolean = true,
  penColor: Color = Color.Black,
  penStrokeWidth: Float = 14f,
  eraserPointColor: Color = Color.LightGray,
  eraserPointRadius: Float = 20f,
  lassoColor: Color = Color.Black,
  lassoBoundBoxColor: Color = Color.Black,
  lassoBoundBoxPadding: Padding = Padding(20, 20, 20, 20),
  laserColor: Color = Color.Black,
  contentBackground: Color = Color.Transparent,
  contentBackgroundImage: ImageBitmap? = null,
  contentBackgroundImageColor: Color = Color.Transparent,
  contentBackgroundImageContentScale: ContentScale = ContentScale.None,
): HandwritingController = remember(
  isZoomable,
  isEraserPointShowed,
  penColor,
  penStrokeWidth,
  eraserPointColor,
  eraserPointRadius,
  lassoColor,
  lassoBoundBoxColor,
  lassoBoundBoxPadding,
  laserColor,
  contentBackground,
  contentBackgroundImage,
) {
  HandwritingController(
    defaultIsZoomable = isZoomable,
    defaultIsEraserPointShowed = isEraserPointShowed,
    defaultPenColor = penColor,
    defaultPenStrokeWidth = penStrokeWidth,
    defaultEraserPointColor = eraserPointColor,
    defaultEraserPointRadius = eraserPointRadius,
    defaultLassoColor = lassoColor,
    defaultLassoBoundBoxColor = lassoBoundBoxColor,
    defaultLassoBoundBoxPadding = lassoBoundBoxPadding,
    defaultLaserColor = laserColor,
    defaultContentBackground = contentBackground,
    defaultContentBackgroundImage = contentBackgroundImage,
    defaultContentBackgroundImageColor = contentBackgroundImageColor,
    defaultContentBackgroundContentScale = contentBackgroundImageContentScale,
  )
}

/**
 * Controller for managing handwriting data, such as pen strokes, eraser actions,
 * and lasso selection. It supports undo/redo functionality, tool modes, and manages
 * the state for various elements like strokes, selected paths, and the lasso area.
 */

@Stable
class HandwritingController internal constructor(
  defaultIsZoomable: Boolean,
  defaultIsEraserPointShowed: Boolean,
  defaultPenColor: Color,
  defaultPenStrokeWidth: Float,
  defaultEraserPointColor: Color,
  defaultEraserPointRadius: Float,
  defaultLassoColor: Color,
  defaultLassoBoundBoxColor: Color,
  defaultLassoBoundBoxPadding: Padding,
  defaultLaserColor: Color,
  defaultContentBackground: Color,
  defaultContentBackgroundImage: ImageBitmap?,
  defaultContentBackgroundImageColor: Color,
  defaultContentBackgroundContentScale: ContentScale,
) {

  /**
   * A mutable state to track refresh ticks for UI updates.
   */
  internal val refreshTick = MutableStateFlow<Int>(0)

  // ==============================
  // Pen-related properties
  // ==============================

  /**
   * A [Paint] object representing the pen's settings.
   */
  internal val penPaint: Paint = defaultPenPaint()

  /**
   * A mutable state for the pen color, initially set to black.
   */

  private var _penColor = mutableStateOf(defaultPenColor)

  /**
   * Public property for retrieving the current pen color.
   */
  val penColor: Color
    get() = _penColor.value

  /**
   * Updates the pen color.
   *
   * @param color The new color to set for the pen.
   */
  fun updatePenColor(color: Color) {
    penPaint.color = color
    _penColor.value = color
  }

  /**
   * A mutable state for the pen's stroke width, initially set to 14f.
   */
  private var _penStrokeWidth = mutableStateOf(defaultPenStrokeWidth)

  /**
   * Public property for retrieving the current pen stroke width.
   */
  val penStrokeWidth: Float
    get() = _penStrokeWidth.value

  /**
   * Updates the pen stroke width.
   *
   * @param width The new stroke width for the pen.
   */
  fun updatePenStrokeWidth(width: Float) {
    penPaint.strokeWidth = width
    _penStrokeWidth.value = width
  }

  // ==============================
  // Eraser-related properties
  // ==============================

  /**
   * A [Paint] object representing the eraser's settings.
   */
  internal val eraserPointPaint: Paint = defaultStrokeEraserPaint()

  /**
   * A mutable state for the eraser point color, initially set to light gray.
   */

  private var _eraserPointColor = mutableStateOf(defaultEraserPointColor)

  /**
   * Public property for retrieving the current eraser point color.
   */
  val eraserPointColor: Color
    get() = _eraserPointColor.value

  /**
   * Updates the eraser point color.
   *
   * @param color The new color to set for the eraser point.
   */
  fun updateEraserPointColor(color: Color) {
    eraserPointPaint.color = color
    _eraserPointColor.value = color
  }

  /**
   * A mutable state for the eraser point radius, initially set to 20f.
   */
  var eraserPointRadius by mutableStateOf(defaultEraserPointRadius)

  /**
   * Updates the eraser point radius.
   *
   * @param radius The new radius to set for the eraser point.
   */
  fun updateEraserPointRadius(radius: Float) {
    eraserPointRadius = radius
  }

  /**
   * Determines whether the eraser point is visible.
   */
  var isEraserPointShowed by mutableStateOf(defaultIsEraserPointShowed)

  /**
   * Updates the visibility of the eraser point.
   *
   * @param isShowed A boolean indicating whether the eraser point should be visible.
   */
  fun updateIsEraserPointShowed(isShowed: Boolean) {
    isEraserPointShowed = isShowed
  }

  // ==============================
  // Lasso selection properties
  // ==============================

  /**
   * A [Paint] object representing the lasso's settings.
   */
  internal val lassoPaint: Paint = defaultLassoPaint()

  /**
   * A mutable state for the lasso color, initially set to black.
   */
  private var _lassoColor = mutableStateOf(defaultLassoColor)

  /**
   * Public property for retrieving the current lasso color.
   */
  val lassoColor: Color
    get() = _lassoColor.value

  /**
   * Updates the lasso color.
   *
   * @param color The new color to set for the lasso.
   */
  fun updateLassoColor(color: Color) {
    lassoPaint.color = color
    _lassoColor.value = color
  }

  /**
   * A [Paint] object representing the lasso bound box's settings.
   */
  internal val lassoBoundBoxPaint: Paint = defaultLassoPaint()

  /**
   * A mutable state for the lasso bound box color, initially set to black.
   */
  private var _lassoBoundBoxColor = mutableStateOf(defaultLassoBoundBoxColor)

  /**
   * Public property for retrieving the current lasso bound box color.
   */
  val lassoBoundBoxColor: Color
    get() = _lassoBoundBoxColor.value

  /**
   * Updates the lasso bound box color.
   *
   * @param color The new color to set for the lasso bound box.
   */
  fun updateLassoBoundBoxColor(color: Color) {
    lassoBoundBoxPaint.color = color
    _lassoBoundBoxColor.value = color
  }

  /**
   * The bounding box representing the selected area during lasso selection.
   */
  internal var lassoBoundBox by mutableStateOf(Rect.Zero)

  /**
   * Resets the lasso bounding box to an empty rectangle.
   */
  internal fun initializeLassoBoundBox() {
    lassoBoundBox = Rect.Zero
  }

  /**
   * The padding applied to the lasso bounding box.
   */
  private var _lassoBoundBoxPadding by mutableStateOf(defaultLassoBoundBoxPadding)

  /**
   * Public property for retrieving the current lasso bound box padding.
   */
  val lassoBoundBoxPadding: Padding
    get() = _lassoBoundBoxPadding

  // ==============================
  // Current laser properties
  // ==============================

  /**
   * A [Paint] object representing the laser's settings.
   */
  internal val laserPaint: Paint = defaultLaserPaint()

  /**
   * A mutable state for the laser color, initially set to black.
   */
  private var _laserColor = mutableStateOf(defaultLaserColor)

  /**
   * Public property for retrieving and updating the laser color.
   */
  val laserColor: Color
    get() = _laserColor.value

  /**
   * Updates the laser color.
   *
   * @param color The new color to set for the laser.
   */
  fun updateLaserColor(color: Color) {
    laserPaint.color = color
    _laserColor.value = color
  }

  /**
   * A collection of all laser paths on the canvas.
   */
  internal val laserPathList = ArrayDeque<Path>()

  /**
   * Clears all laser handwriting paths from the canvas.
   */
  internal fun clearLaserPaths() {
    laserPathList.clear()
  }

  /**
   * Determines whether the laser drawing is finished.
   */
  internal var isLaserEnd = MutableStateFlow<Boolean>(false)

  // ==============================
  // Current tool-related properties
  // ==============================

  /**
   * The currently active paint settings (either pen or eraser).
   */
  internal var currentPaint by mutableStateOf(penPaint)

  /**
   * The current touch event handler based on the selected tool.
   */
  internal var currentTouchEvent: ToolTouchEvent by mutableStateOf(PenTouchEvent(this))

  // ==============================
  // Handwriting canvas properties
  // ==============================

  /**
   * The background color of the handwriting canvas.
   */
  val contentBackground by mutableStateOf(defaultContentBackground)

  /**
   * A mutable state holding the background image of the handwriting canvas.
   */
  private var _contentBackgroundImageBitmap = mutableStateOf(defaultContentBackgroundImage)

  /**
   * The public property to retrieve the current background image.
   */
  val contentBackgroundImageBitmap: ImageBitmap?
    get() = _contentBackgroundImageBitmap.value

  /**
   * A mutable state holding the overlay color applied to the background image.
   */
  private var _contentBackgroundImageColor = mutableStateOf(defaultContentBackgroundImageColor)

  /**
   * The public property to retrieve the current overlay color of the background image.
   */
  val contentBackgroundImageColor: Color
    get() = _contentBackgroundImageColor.value

  /**
   * A mutable state holding the content scaling mode for the background image.
   */
  private var _contentBackgroundImageContentScale = mutableStateOf(defaultContentBackgroundContentScale)

  /**
   * The public property to retrieve the current content scale mode of the background image.
   */
  val contentBackgroundImageContentScale: ContentScale
    get() = _contentBackgroundImageContentScale.value

  /**
   * Updates the background image and its scaling mode.
   *
   * @param imageBitmap The new image to set as the background.
   * @param contentScale The content scaling mode for the background image.
   */
  fun updateContentBackgroundImageBitmap(imageBitmap: ImageBitmap, contentScale: ContentScale) {
    _contentBackgroundImageBitmap.value = imageBitmap
    _contentBackgroundImageContentScale.value = contentScale
  }

  /**
   * A mutable state indicating whether zooming is enabled.
   */
  private var _isZoomable = mutableStateOf(defaultIsZoomable)

  /**
   * A public property to check if zooming is enabled.
   */
  val isZoomable: Boolean
    get() = _isZoomable.value

  // ==============================
  // Path storage collections
  // ==============================

  /**
   * A collection of all handwriting paths on the canvas.
   */
  internal val handwritingPaths = ArrayDeque<HandwritingPath>()

  /**
   * Clears all handwriting paths from the canvas.
   */
  internal fun clearHandWritingPaths() {
    handwritingPaths.clear()
  }

  /**
   * A set of currently selected handwriting paths.
   */
  internal val selectedHandwritingPaths = mutableSetOf<HandwritingPath>()

  /**
   * Clears all selected handwriting paths.
   */
  internal fun clearSelectedHandwritingPaths() {
    selectedHandwritingPaths.clear()
  }

  /** Sets [Paint], [PenTouchEvent] to the [toolMode]. */
  fun setToolMode(toolMode: ToolMode) {
    when (toolMode) {
      ToolMode.PenMode -> {
        currentPaint = penPaint
        currentTouchEvent = PenTouchEvent(this)
      }

      ToolMode.EraserMode -> {
        currentPaint = eraserPointPaint
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

      ToolMode.LineLaserMode -> {
        currentPaint = laserPaint
        currentTouchEvent = LineLaserPointerTouchEvent(this)
      }
    }

    initializeTouchEvent()
    initializeSelection()
  }

  /**
   * Initializes the touch event by calling the onTouchInitialize method.
   */
  private fun initializeTouchEvent() {
    currentTouchEvent.onTouchInitialize()
  }

  /**
   * Adds a new handwriting path to the list of recorded paths.
   * This method updates the list of handwriting paths and triggers a refresh tick
   * to notify the UI about the change.
   *
   * @param path The handwriting path to be added to the list.
   */
  internal fun addHandWritingPath(path: HandwritingPath) {
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
  internal fun addHandWritingPaths(paths: List<HandwritingPath>) {
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
  internal fun addHandWritingPath(
    renderedPath: Path,
    hitAreaPath: Path,
    offsets: List<Offset>,
  ) {
    execute(
      InsertOperation(
        controller = this@HandwritingController,
        path = HandwritingPath(
          renderedPath = renderedPath,
          hitAreaPath = hitAreaPath,
          initialPoints = offsets,
          paint = Paint().copy(penPaint),
        ),
      ),
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
  internal fun removeHandWritingPath(path: HandwritingPath) {
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
  internal fun removeHandWritingPath(path: Path) {
    val hitResult = hitHandWritingPath(path)
    if (hitResult.isHit) {
      hitResult.path?.let {
        execute(
          RemoveOperation(
            controller = this@HandwritingController,
            path = it,
          ),
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
          path = data,
        )
      }
    }

    return HitResult(
      isHit = false,
      path = null,
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
  internal fun selectHandWritingPath(
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
      lassoBoundBox = tempBounds.addPadding(_lassoBoundBoxPadding)
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
  internal fun translateHandWritingPaths(
    translateOffset: Offset,
  ) {
    execute(
      TranslateOperation(
        controller = this@HandwritingController,
        paths = selectedHandwritingPaths.toMutableSet(),
        offset = translateOffset,
      ),
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
  internal fun translateLassoBoundBox(matrix: Matrix) {
    lassoBoundBox = lassoBoundBox.translate(matrix)
  }

  /**
   * Clears all handwriting paths from the canvas.
   * This function removes all paths from the `handwritingPaths` list and resets the state.
   * It is typically used when the user wants to start with a fresh canvas or clear the drawing.
   */
  fun clearAllHandWritingPaths() {
    initializeTouchEvent()
    clearHandWritingPaths()
    updateRefreshTick()
  }

  /**
   * Initializes the selection state by clearing the lasso bounding box and selected handwriting paths.
   * This function resets the selection, ensuring that no paths are currently selected, and the lasso
   * bounding box is cleared.
   * It is typically used when starting a new selection or when resetting the current selection state.
   */
  internal fun initializeSelection() {
    initializeLassoBoundBox()
    clearSelectedHandwritingPaths()
    updateRefreshTick()
  }

  /**
   * Updates the refresh tick by triggering the `updateTick` method on the `refreshTick` state.
   * This function is used to signal that a change has occurred and the UI may need to be refreshed.
   * Typically used after modifying any data that impacts the display of handwriting paths or selection state.
   */
  internal fun updateRefreshTick() {
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
  internal val undoOperations: ArrayDeque<Operation> = ArrayDeque()

  /**
   * A deque (double-ended queue) holding the operations for redoing.
   * Stores the sequence of operations that can be reapplied after an undo.
   */
  internal val redoOperations: ArrayDeque<Operation> = ArrayDeque()

  /**
   * Executes the given operation.
   *
   * If the operation is successful, it is added to the undo stack, and the redo stack is cleared.
   * This is done to maintain the correct state for undo/redo functionality.
   *
   * @param operation The operation to be executed.
   */
  internal fun execute(operation: Operation) {
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

    initializeTouchEvent()
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

    initializeTouchEvent()
    initializeSelection()
    updateUndoRedoState()
  }

  /**
   * Updates the state of the undo and redo flags (`canUndo` and `canRedo`).
   * Sets `canUndo` to true if there are operations in the undo stack,
   * and `canRedo` to true if there are operations in the redo stack.
   */
  internal fun updateUndoRedoState() {
    canUndo = !undoOperations.isEmpty()
    canRedo = !redoOperations.isEmpty()
  }
}

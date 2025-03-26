package com.henni.writebuddy.tool.tape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.util.fastForEachReversed
import com.henni.writebuddy.extension.overlaps
import com.henni.writebuddy.extension.updateTick
import com.henni.writebuddy.model.copy
import com.henni.writebuddy.model.defaultTapePaint
import com.henni.writebuddy.tool.ToolController
import com.henni.writebuddy.tool.ToolTouchEvent

/**
 * Creates and remembers a [TapeController] instance.
 *
 * @param paint The default paint used for drawing the tape. Defaults to `defaultTapePaint()`.
 * @param imagePaint The default paint used for drawing the tape image. Defaults to a new `Paint()`.
 * @param selectedTapeAlpha The alpha value for the selected tape. Defaults to `0.2f`.
 * @param tapeWidth The width of the tape. Defaults to `80f`.
 * @param tapeCornerRadius The corner radius for the tape. Defaults to `CornerRadius(10f, 10f)`.
 *
 * @return A remembered [TapeController] instance.
 */

@Composable
fun rememberTapeController(
  paint: Paint = defaultTapePaint(),
  imagePaint: Paint = Paint(),
  selectedTapeAlpha: Float = 0.2f,
  tapeCornerRadius: CornerRadius = CornerRadius(10f, 10f),
): TapeController = remember {
  TapeController(
    defaultPaint = paint,
    defaultImagePaint = imagePaint,
    defaultSelectedTapeAlpha = selectedTapeAlpha,
    defaultCornerRadius = tapeCornerRadius,
  )
}

/**
 * Controller responsible for managing tape-related actions, including drawing the tape,
 * handling touch events, and maintaining the state of the tape items.
 *
 * @param defaultPaint The default paint used for drawing the tape.
 * @param defaultImagePaint The default paint used for drawing the tape image.
 * @param defaultSelectedTapeAlpha The default alpha value for selected tapes.
 * @param defaultCornerRadius The default corner radius of the tape.
 */

@Stable
class TapeController internal constructor(
  defaultPaint: Paint,
  defaultImagePaint: Paint,
  defaultSelectedTapeAlpha: Float,
  defaultCornerRadius: CornerRadius,
) : ToolController {

  /** The touch event handler for tape-related actions. */
  private val _touchEvent = mutableStateOf(
    TapeTouchEvent(
      this,
    ),
  )

  /**
   * The touch event handler for the tape.
   *
   * @return The current touch event instance.
   */
  override val touchEvent: ToolTouchEvent
    get() = _touchEvent.value

  /**
   * Gets the current paint style for the tape.
   *
   * @return The paint style used for the tape.
   */
  private var _paint = mutableStateOf(defaultPaint)

  val paint: Paint
    get() = _paint.value

  /**
   * Updates the paint style for the tape.
   *
   * @param value The new paint style to be applied.
   */
  fun updateTapePaint(value: Paint) {
    _paint.value = _paint.value.copy(value)
  }

  /**
   * Updates the stroke width of the tape.
   *
   * @param value The new stroke width for the tape.
   */
  fun updateTapeWidth(value: Float) {
    _paint.value.strokeWidth = value
    _paint.value = Paint().copy(_paint.value)
  }

  /**
   * Updates the color of the tape.
   *
   * @param value The new color to be applied to the tape.
   */
  fun updateTapeColor(value: Color) {
    _paint.value.color = value
    _paint.value = Paint().copy(_paint.value)
  }

  /** The paint style used for drawing the tape image. */
  private var _imagePaint = mutableStateOf(defaultImagePaint)

  /**
   * Gets the current paint style for the tape image.
   *
   * @return The paint style used for the tape image.
   */
  val imagePaint: Paint
    get() = _imagePaint.value

  /**
   * Updates the paint style for the tape image.
   *
   * @param value The new paint style to be applied.
   */
  fun updateTapeImagePaint(value: Paint) {
    _imagePaint.value = value
  }

  /**
   * Updates the stroke width of the tape image.
   *
   * @param value The new stroke width for the tape image.
   */
  fun updateTapeImageWidth(value: Float) {
    _imagePaint.value.strokeWidth = value
    _imagePaint.value = Paint().copy(imagePaint)
  }

  /**
   * Updates the color of the tape image.
   *
   * @param value The new color to be applied to the tape image.
   */
  fun updateTapeImageColor(value: Color) {
    _imagePaint.value.color = value
    _imagePaint.value = Paint().copy(imagePaint)
  }

  /** The alpha value used for selected tape items. */
  private val _selectedTapeAlpha = mutableStateOf(defaultSelectedTapeAlpha)

  /**
   * Gets the current alpha value for selected tape items.
   *
   * @return The alpha value for selected tape items.
   */
  val selectedTapeAlpha: Float
    get() = _selectedTapeAlpha.value

  /**
   * Updates the alpha value for selected tape items.
   *
   * @param value The new alpha value for selected tape items.
   */
  fun updateSelectedTapeAlpha(value: Float) {
    _selectedTapeAlpha.value = value
  }

  /** The corner radius for the tape. */
  private val _cornerRadius = mutableStateOf(defaultCornerRadius)

  /**
   * Gets the current corner radius for the tape.
   *
   * @return The corner radius of the tape.
   */
  val cornerRadius: CornerRadius
    get() = _cornerRadius.value

  /**
   * Updates the corner radius for the tape.
   *
   * @param value The new corner radius to be applied.
   */
  fun updateCornerRadius(value: CornerRadius) {
    _cornerRadius.value = value
  }

  /** A list holding all the tape items. */
  private val _tapeItemList = mutableStateListOf<TapeItem>()

  /**
   * Gets a copy of the list of all tape items.
   *
   * @return A list of tape items.
   */
  val tapeItemList: List<TapeItem>
    get() = _tapeItemList.toList()

  /**
   * Adds a new tape item to the list of tape items.
   *
   * @param value The tape item to be added.
   */
  fun addTapeItem(value: TapeItem) {
    _tapeItemList.add(value)
  }

  /** A map of selected tape items. */
  private val _selectedTapeItems = mutableStateMapOf<String, Boolean>()

  /**
   * Gets a map of selected tape items.
   *
   * @return A map of selected tape items.
   */
  val selectedTapeItems: Map<String, Boolean>
    get() = _selectedTapeItems.toMap()

  /**
   * Selects or deselects a tape item based on the given point.
   *
   * @param point The point where the selection occurs.
   */
  fun selectTapeItem(point: Offset) {
    val pointPath = Path().apply {
      addOval(
        Rect(
          center = point,
          radius = 10f,
        ),
      )
    }

    tapeItemList.fastForEachReversed { item ->

      if (overlaps(pointPath, item.path)) {
        if (_selectedTapeItems.contains(item.id)) {
          _selectedTapeItems.remove(item.id)
        } else {
          _selectedTapeItems[item.id] = true
        }
      }
    }

    updateInvalidateTick()
  }

  /** A state representing whether the multi-touch mode is active. */
  private var _isMultiTouched = mutableStateOf(false)

  /**
   * Checks if multi-touch mode is active.
   *
   * @return True if multi-touch mode is active, otherwise false.
   */
  override val isMultiTouched: Boolean
    get() = _isMultiTouched.value

  /**
   * Updates the multi-touch state of the controller.
   *
   * @param value The new multi-touch state.
   */
  fun updateIsMultiTouched(value: Boolean) {
    _isMultiTouched.value = value
  }

  /** A state representing the invalidate tick for re-rendering. */
  private var _invalidateTick = mutableStateOf(0)

  /**
   * The invalidate tick used to trigger re-rendering of the component.
   *
   * @return The current invalidate tick value.
   */
  override val invalidateTick: Int
    get() = _invalidateTick.value

  /**
   * Updates the invalidate tick to trigger a re-render.
   */
  override fun updateInvalidateTick() {
    _invalidateTick.updateTick()
  }
}

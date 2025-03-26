package com.henni.writebuddy.tool.laser

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import com.henni.writebuddy.extension.updateTick
import com.henni.writebuddy.model.copy
import com.henni.writebuddy.model.defaultLaserPaint
import com.henni.writebuddy.tool.ToolController
import com.henni.writebuddy.tool.ToolTouchEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Composable function that animates the alpha (opacity) of the laser path based on its state.
 * The animation is triggered when the laser ends, and its opacity smoothly transitions from 1f to 0f.
 *
 * @param controller The LaserPointerController responsible for controlling the laser pointer's state.
 * @param finishedListener A callback function that is triggered when the laser reaches the end.
 *
 * @return A state that represents the animated alpha value for the laser path.
 */
@Composable
fun animateLaserAlphaFloatAsState(
  controller: LaserPointerController,
  finishedListener: () -> Unit,
): State<Float> {
  var isLaserEnd by remember { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()

  val laserPathAlpha = animateFloatAsState(
    targetValue = if (isLaserEnd) 0f else 1f,
    animationSpec = if (isLaserEnd) tween(1000) else tween(0),
    finishedListener = {
      if (it == 0f) {
        finishedListener()
      }
    },
  )

  LaunchedEffect(Unit) {
    coroutineScope.launch {
      controller.isLaserEnd.collect {
        if (it) {
          delay(1000)
          isLaserEnd = true
        } else {
          isLaserEnd = false
        }
      }
    }
  }

  return laserPathAlpha
}

/**
 * Composable function to create and remember the LaserPointerController.
 *
 * @param paint The paint style used for the laser pointer. Default value is the provided defaultLaserPaint.
 * @return The LaserPointerController instance.
 */
@Composable
fun rememberLaserPointerController(
  paint: Paint = defaultLaserPaint(),
): LaserPointerController = remember {
  LaserPointerController(
    defaultPaint = paint,
  )
}

/**
 * The controller responsible for managing laser pointer actions, including touch events, paint settings,
 * and laser path state.
 *
 * @param defaultPaint The default paint used for drawing the Laser Pointer.
 */
@Stable
class LaserPointerController internal constructor(
  defaultPaint: Paint,
) : ToolController {

  /** The touch event handler for laser pointer -related actions. */
  private val _touchEvent = mutableStateOf(
    LineLaserPointerTouchEvent(
      this,
    ),
  )

  /**
   * The touch event handler for the laser pointer.
   *
   * @return The current touch event instance.
   */
  override val touchEvent: ToolTouchEvent
    get() = _touchEvent.value

  /**
   * The current paint style used for drawing the laser pointer.
   *
   * @return The paint style used by the laser pointer.
   */
  private var _paint = mutableStateOf(defaultPaint)

  /**
   * Gets the current paint style.
   *
   * @return The paint style used for the laser pointer.
   */
  val paint: Paint
    get() = _paint.value

  /**
   * Updates the paint style for the laser pointer.
   *
   * @param value The new paint style to be applied.
   */
  fun updateLaserPaint(value: Paint) {
    _paint.value = _paint.value.copy(value)
  }

  /**
   * Updates the color of the laser pointer.
   *
   * @param value The new color to be applied to the laser pointer.
   */
  fun updateLaserColor(value: Color) {
    _paint.value.color = value
    _paint.value = Paint().copy(_paint.value)
  }

  /**
   * Updates the stroke width of the laser pointer.
   *
   * @param value The new stroke width for the laser pointer.
   */
  fun updateLaserStrokeWidth(value: Float) {
    _paint.value.strokeWidth = value
    _paint.value = Paint().copy(_paint.value)
  }

  /**
   * A flow representing whether the laser path has ended or not.
   */
  internal var isLaserEnd = MutableStateFlow<Boolean>(false)

  /**
   * A list holding the laser paths drawn by the laser pointer.
   *
   * @return A list of laser paths.
   */
  private val _laserPathList = mutableStateListOf<Path>()

  /**
   * Gets a copy of the laser path list.
   *
   * @return A list of laser paths.
   */
  val laserPathList: List<Path>
    get() = _laserPathList.toList()

  /**
   * Adds a new laser path to the path list.
   *
   * @param value The laser path to be added.
   */
  fun addLaserPath(value: Path) {
    _laserPathList.add(value)
  }

  /**
   * Clears all the laser paths.
   */
  fun clearLaserPaths() {
    _laserPathList.clear()
  }

  /**
   * A state representing whether the laser pointer is in a multi-touch state.
   */
  private var _isMultiTouched = mutableStateOf(false)

  /**
   * Gets whether the laser pointer is in a multi-touch state.
   *
   * @return True if the laser pointer is being multi-touched, otherwise false.
   */
  override val isMultiTouched: Boolean
    get() = _isMultiTouched.value

  /**
   * Updates the multi-touch state of the laser pointer.
   *
   * @param value The new multi-touch state.
   */
  fun updateIsMultiTouched(value: Boolean) {
    _isMultiTouched.value = value
  }

  /**
   * A state representing the invalidate tick, which is used for re-rendering the component.
   *
   * @return The current invalidate tick value.
   */
  private var _invalidateTick = mutableStateOf(0)

  /**
   * The invalidate tick used to trigger re-rendering.
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

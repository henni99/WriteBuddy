package com.henni.writebuddy.tool.tape

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a sticky text item that can be attached to the canvas.
 * This class contains properties related to the text, including its position, scaling, and properties.
 *
 * @param text The text content of the sticky item. Defaults to "input".
 * @param id The unique identifier for the sticky text item, generated using `Uuid`.
 * @param type The type of the sticky item (e.g., text, image, etc.).
 * @param isFocus A boolean indicating whether the sticky item is currently focused.
 * @param firstPoint The initial position of the sticky text item on the canvas.
 * @param translate The offset used for translating the sticky text item on the canvas.
 * @param scaleFactor The scaling factor for the sticky text item.
 * @param scaleOffset The offset used for scaling the sticky text item.
 * @param property The properties related to the sticky text item (e.g., font, size, color).
 */

@Immutable
data class TapeItem @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val bound: Rect,
    val startPoint: Offset,
    val paint: Paint,
    val imagePaint: Paint,
    val path: Path,
    val imagePath: Path = Path()
)

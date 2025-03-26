package com.henni.writebuddy.sticky.text.common

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import com.henni.writebuddy.Attachable
import com.henni.writebuddy.sticky.Property
import com.henni.writebuddy.sticky.StickyType
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
data class StickyTextItem @OptIn(ExperimentalUuidApi::class) constructor(
    val text: String = "input",
    override val id: String = Uuid.random().toString(),
    override val type: StickyType,
    override val isFocus: Boolean = false,
    override val firstPoint: Offset,
    override val translate: Offset = Offset.Zero,
    override val scaleFactor: Float = 1f,
    override val scaleOffset: Offset = Offset.Zero,
    override val property: Property
) : Attachable() {
    override fun copySelf(
        id: String,
        type: StickyType,
        isFocus: Boolean,
        firstPoint: Offset,
        translate: Offset,
        scaleOffset: Offset,
        scaleFactor: Float,
        property: Property
    ): Attachable {
        return copy(
            id = id,
            type = type,
            isFocus = isFocus,
            firstPoint = firstPoint,
            translate = translate,
            scaleFactor = scaleFactor,
            scaleOffset = scaleOffset,
            property = property
        )
    }
}

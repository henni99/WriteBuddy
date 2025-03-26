package com.henni.writebuddy.sticky.image.imageBitmap

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import com.henni.writebuddy.Attachable
import com.henni.writebuddy.sticky.Property
import com.henni.writebuddy.sticky.StickyType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents an image bitmap item that can be attached to a canvas or document.
 * This class stores transformation properties such as position, scale, and translation.
 *
 * @property id Unique identifier for the item.
 * @property type The type of sticky item (default is BITMAP_IMAGE).
 * @property isFocus Indicates whether the item is currently focused.
 * @property firstPoint The initial position of the item.
 * @property translate Offset representing the translation of the item.
 * @property scaleFactor Scaling factor applied to the item.
 * @property scaleOffset Additional offset applied during scaling.
 * @property property Additional properties associated with the item.
 */

@Immutable
data class ImageBitmapItem
@OptIn(ExperimentalUuidApi::class)
constructor(
  override val id: String = Uuid.random().toString(),
  override val type: StickyType = StickyType.BITMAP_IMAGE,
  override val isFocus: Boolean = false,
  override val firstPoint: Offset,
  override val translate: Offset = Offset.Zero,
  override val scaleFactor: Float = 1f,
  override val scaleOffset: Offset = Offset.Zero,
  override val property: Property,
) : Attachable() {
  override fun copySelf(
    id: String,
    type: StickyType,
    isFocus: Boolean,
    firstPoint: Offset,
    translate: Offset,
    scaleOffset: Offset,
    scaleFactor: Float,
    property: Property,
  ): Attachable = copy(
    id = id,
    type = type,
    isFocus = isFocus,
    firstPoint = firstPoint,
    translate = translate,
    scaleFactor = scaleFactor,
    scaleOffset = scaleOffset,
    property = property,
  )
}

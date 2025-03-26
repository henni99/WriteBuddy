package com.henni.writebuddy

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import com.henni.writebuddy.sticky.Property
import com.henni.writebuddy.sticky.StickyType


@Stable
abstract class Attachable {

    /**
     * The unique identifier for the attachable object.
     */
    abstract val id: String

    /**
     * The type of the attachable object (e.g., sticky note, image, or text box).
     */
    abstract val type: StickyType

    /**
     * A boolean indicating whether the attachable object is currently focused.
     * This property is used to manage the selection state of the object (e.g., for editing).
     */
    abstract val isFocus: Boolean

    /**
     * The initial position of the attachable object on the canvas.
     * This represents the point where the object was first placed before any transformations (such as move or scale).
     */
    abstract val firstPoint: Offset

    /**
     * The translation offset applied to the attachable object.
     * This value is used to move the object from its original position.
     */
    abstract val translate: Offset

    /**
     * The scaling offset applied to the attachable object.
     * This offset determines how much the object is scaled from its original size.
     */
    abstract val scaleOffset: Offset

    /**
     * The scaling factor for the attachable object.
     * This value defines how much the object should be scaled in size.
     */
    abstract val scaleFactor: Float

    /**
     * The properties related to the attachable object (e.g., text formatting, color, font size).
     * This property contains various settings that define the appearance and behavior of the object.
     */
    abstract val property: Property

    /**
     * Creates and returns a copy of the attachable object with updated properties.
     * This method allows for creating a new instance with specific modifications, while preserving the original attributes.
     *
     * @param id The new unique identifier for the object. Defaults to the current ID.
     * @param type The new type of the object. Defaults to the current type.
     * @param isFocus A boolean indicating whether the new object should be in focus. Defaults to the current focus state.
     * @param firstPoint The new initial position for the object. Defaults to the current first point.
     * @param translate The new translation offset for the object. Defaults to the current translation.
     * @param scaleOffset The new scaling offset. Defaults to the current scaling offset.
     * @param scaleFactor The new scaling factor. Defaults to the current scaling factor.
     * @param property The new properties for the object. Defaults to the current properties.
     *
     * @return A new instance of the object with updated properties.
     */
    abstract fun copySelf(
        id: String = this.id,
        type: StickyType = this.type,
        isFocus: Boolean = this.isFocus,
        firstPoint: Offset = this.firstPoint,
        translate: Offset = this.translate,
        scaleOffset: Offset = this.scaleOffset,
        scaleFactor: Float = this.scaleFactor,
        property: Property = this.property
    ): Attachable
}

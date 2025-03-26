package com.henni.writebuddy.sticky

/**
 * Enum class that represents different types of sticky items.
 * Each sticky item can have one of the following types, each corresponding to a specific kind of sticky element.
 */

enum class StickyType {

    /**
     * Represents a sticky note, typically a post-it note style.
     * This is a simple sticky item, often used for text notes.
     */
    POST_IT,

    /**
     * Represents a sticky item that displays a painter image.
     * This could be an image that is drawn or rendered in some way.
     */
    PAINTER_IMAGE,

    /**
     * Represents a sticky item that displays a vector image.
     * Vector images are scalable graphics created using mathematical formulas.
     */
    VECTOR_IMAGE,

    /**
     * Represents a sticky item that displays a bitmap image.
     * Bitmap images are pixel-based graphics, commonly used for photographs and detailed images.
     */
    BITMAP_IMAGE,

    /**
     * Represents a sticky item that functions like a text box.
     * This can be used for larger blocks of text or for more complex textual content.
     */
    TEXT_BOX
}
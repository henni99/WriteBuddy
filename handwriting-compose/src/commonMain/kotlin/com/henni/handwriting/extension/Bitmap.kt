package com.henni.handwriting.extension

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.unit.IntSize

/**
 * Creates and returns a new `ImageBitmap` with the specified size and ARGB8888 color format.
 * The `ImageBitmap` is initialized with the given width and height.
 *
 * @param size The size (width and height) of the bitmap to create.
 * @return A new `ImageBitmap` with the specified size and ARGB8888 configuration.
 */
internal inline fun getBitmap(size: IntSize): ImageBitmap {
    return ImageBitmap(
        size.width,
        size.height,
        ImageBitmapConfig.Argb8888
    )
}
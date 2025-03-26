package com.henni.writebuddy.sticky.image.imageBitmap

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.henni.writebuddy.sticky.Property

/**
 * Represents the properties of an image bitmap used in a drawable element.
 * This class defines various attributes such as size, alignment, and color properties.
 *
 * @property size The size of the image in Dp.
 * @property imageBitmap The bitmap image associated with this property (nullable).
 * @property color The color overlay applied to the image.
 * @property alignment The alignment of the image within its container.
 * @property contentScale Defines how the image should be scaled within its bounds.
 * @property alpha The alpha transparency value of the image.
 * @property colorFilter Optional color filter applied to the image.
 */

@Immutable
data class ImageBitmapProperty(
    val size: DpSize,
    val imageBitmap: ImageBitmap?,
    val color: Color,
    val alignment: Alignment,
    val contentScale: ContentScale,
    val alpha: Float,
    val colorFilter: ColorFilter?
) : Property {
    companion object {

        /**
         * Default property values for an image bitmap.
         */
        @Stable
        val DEFAULT = ImageBitmapProperty(
            size = DpSize(200.dp, 200.dp),
            imageBitmap = null,
            color = Color.Transparent,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            alpha = DefaultAlpha,
            colorFilter = null
        )
    }
}
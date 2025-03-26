package com.henni.writebuddy.sticky.image.painter

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.henni.writebuddy.sticky.Property

/**
 * Represents the properties of a painter image used in a drawable element.
 * This class defines various attributes such as size, alignment, color properties, and painting content.
 *
 * @property size The size of the painter image in Dp.
 * @property painter The painter associated with this property (nullable).
 * @property color The color overlay applied to the painter image.
 * @property alignment The alignment of the painter image within its container.
 * @property contentScale Defines how the painter image should be scaled within its bounds.
 * @property alpha The alpha transparency value of the painter image.
 * @property colorFilter Optional color filter applied to the painter image.
 */

@Immutable
data class PainterImageProperty(
    val size: DpSize,
    val painter: Painter?,
    val color: Color,
    val alignment: Alignment,
    val contentScale: ContentScale,
    val alpha: Float,
    val colorFilter: ColorFilter?
) : Property {
    companion object {

        /**
         * Default property values for a painter image.
         */
        @Stable
        val DEFAULT = PainterImageProperty(
            size = DpSize(200.dp, 200.dp),
            painter = null,
            color = Color.Transparent,
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit,
            alpha = DefaultAlpha,
            colorFilter = null
        )
    }
}




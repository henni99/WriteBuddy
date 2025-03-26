package com.henni.writebuddy.sticky.image.imageVector

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.henni.writebuddy.sticky.Property

/**
 * Represents the properties of a vector image used in a drawable element.
 * This class defines various attributes such as size, alignment, and color properties.
 *
 * @property size The size of the vector image in Dp.
 * @property imageVector The vector image associated with this property (nullable).
 * @property color The color overlay applied to the vector image.
 * @property alignment The alignment of the vector image within its container.
 * @property contentScale Defines how the vector image should be scaled within its bounds.
 * @property alpha The alpha transparency value of the vector image.
 * @property colorFilter Optional color filter applied to the vector image.
 */

@Immutable
data class VectorImageProperty(
  val size: DpSize,
  val imageVector: ImageVector?,
  val color: Color,
  val alignment: Alignment,
  val contentScale: ContentScale,
  val alpha: Float,
  val colorFilter: ColorFilter?,
) : Property {
  companion object {

    /**
     * Default property values for an vector image.
     */
    @Stable
    val DEFAULT = VectorImageProperty(
      size = DpSize(200.dp, 200.dp),
      imageVector = null,
      color = Color.Transparent,
      alignment = Alignment.Center,
      contentScale = ContentScale.Fit,
      alpha = DefaultAlpha,
      colorFilter = null,
    )
  }
}

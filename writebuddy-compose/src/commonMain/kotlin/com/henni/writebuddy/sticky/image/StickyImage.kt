package com.henni.writebuddy.sticky.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.henni.writebuddy.sticky.Sticky
import com.henni.writebuddy.sticky.image.imageBitmap.ImageBitmapItem
import com.henni.writebuddy.sticky.image.imageBitmap.ImageBitmapProperty
import com.henni.writebuddy.sticky.image.imageVector.VectorImageItem
import com.henni.writebuddy.sticky.image.imageVector.VectorImageProperty
import com.henni.writebuddy.sticky.image.painter.PainterImageItem
import com.henni.writebuddy.sticky.image.painter.PainterImageProperty

/**
 * A composable function that represents a sticky image, which can be moved, zoomed, or deleted.
 * This function displays an image using the provided [PainterImageProperty] and [PainterImageItem].
 *
 * @param modifier The modifier to be applied to the sticky image.
 * @param property The properties related to the painter image, including size, color, alignment, etc.
 * @param image The painter image item containing the actual image data.
 * @param onStickyMoved A callback invoked when the sticky image is moved. It provides the new offset.
 * @param onZoomChanged A callback invoked when the zoom level changes. It provides the image ID, scale factor, and offset.
 * @param onDeleteSticky A callback invoked when the sticky image is deleted.
 */

@Composable
fun StickyImage(
  modifier: Modifier = Modifier,
  property: PainterImageProperty,
  image: PainterImageItem,
  onStickyMoved: (Offset) -> Unit,
  onZoomChanged: (String, Float, Offset) -> Unit,
  onDeleteSticky: (PainterImageItem) -> Unit,
) {
  Sticky(
    attachable = image,
    stickySize = property.size,
    onStickyMoved = { offset ->
      onStickyMoved(offset)
    },
    onZoomChanged = { scale, offset ->
      onZoomChanged(image.id, scale, offset)
    },
  ) {
    property.painter?.let {
      Image(
        painter = it,
        contentDescription = null,
        modifier = modifier
          .size(property.size),
        alignment = property.alignment,
        contentScale = property.contentScale,
        alpha = property.alpha,
        colorFilter = property.colorFilter,
      )
    }
  }
}

/**
 * A composable function that represents a sticky vector image, which can be moved, zoomed, or deleted.
 * This function displays an image using the provided [VectorImageProperty] and [VectorImageItem].
 *
 * @param modifier The modifier to be applied to the sticky vector image.
 * @param property The properties related to the vector image, including size, color, alignment, etc.
 * @param image The vector image item containing the actual image data.
 * @param onStickyMoved A callback invoked when the sticky image is moved. It provides the new offset.
 * @param onZoomChanged A callback invoked when the zoom level changes. It provides the image ID, scale factor, and offset.
 * @param onDeleteSticky A callback invoked when the sticky image is deleted.
 */

@Composable
fun StickyImage(
  modifier: Modifier = Modifier,
  property: VectorImageProperty,
  image: VectorImageItem,
  onStickyMoved: (Offset) -> Unit,
  onZoomChanged: (String, Float, Offset) -> Unit,
  onDeleteSticky: (VectorImageItem) -> Unit,
) {
  Sticky(
    attachable = image,
    stickySize = property.size,
    onStickyMoved = { offset ->
      onStickyMoved(offset)
    },
    onZoomChanged = { scale, offset ->
      onZoomChanged(image.id, scale, offset)
    },
  ) {
    property.imageVector?.let {
      Image(
        imageVector = it,
        contentDescription = null,
        modifier = modifier
          .size(property.size),
        alignment = property.alignment,
        contentScale = property.contentScale,
        alpha = property.alpha,
        colorFilter = property.colorFilter,
      )
    }
  }
}

/**
 * A composable function that represents a sticky image bitmap, which can be moved, zoomed, or deleted.
 * This function displays an image using the provided [ImageBitmapProperty] and [ImageBitmapItem].
 *
 * @param modifier The modifier to be applied to the sticky image bitmap.
 * @param property The properties related to the image bitmap, including size, color, alignment, etc.
 * @param image The image bitmap item containing the actual image data.
 * @param onStickyMoved A callback invoked when the sticky image is moved. It provides the new offset.
 * @param onZoomChanged A callback invoked when the zoom level changes. It provides the image ID, scale factor, and offset.
 * @param onDeleteSticky A callback invoked when the sticky image is deleted.
 */

@Composable
fun StickyImage(
  modifier: Modifier = Modifier,
  property: ImageBitmapProperty,
  image: ImageBitmapItem,
  onStickyMoved: (Offset) -> Unit,
  onZoomChanged: (String, Float, Offset) -> Unit,
  onDeleteSticky: (ImageBitmapItem) -> Unit,
) {
  Sticky(
    attachable = image,
    stickySize = property.size,
    onStickyMoved = {
      onStickyMoved(it)
    },
    onZoomChanged = { scale, offset ->
      onZoomChanged(image.id, scale, offset)
    },
  ) {
    property.imageBitmap?.let {
      Image(
        bitmap = it,
        modifier = modifier
          .size(property.size),
        contentDescription = null,
        alignment = property.alignment,
        contentScale = property.contentScale,
        alpha = property.alpha,
        colorFilter = property.colorFilter,
      )
    }
  }
}
